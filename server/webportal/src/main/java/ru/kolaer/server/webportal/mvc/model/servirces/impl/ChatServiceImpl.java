package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatInfoService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final String PUBLIC_MAIN_GROUP_NAME = "КолАЭР";
    private final String PUBLIC_MAIN_ROOM_ID = "main";
    private final Map<String, ChatGroupDto> groups = new HashMap<>();
    private ChatGroupDto mainGroup;

    private final String key;
    private final ChatInfoService chatInfoService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthenticationService authenticationService;
    private final AccountDao accountDao;
    private final ChatMessageDao chatMessageDao;
    private final ChatMessageConverter chatMessageConverter;

    public ChatServiceImpl(@Value("${secret_key:secret_key}") String key,
                           ChatInfoService chatInfoService,
                           SimpMessagingTemplate simpMessagingTemplate,
                           AuthenticationService authenticationService,
                           AccountDao accountDao, ChatMessageDao chatMessageDao,
                           ChatMessageConverter chatMessageConverter) {
        this.key = key;
        this.chatInfoService = chatInfoService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authenticationService = authenticationService;
        this.accountDao = accountDao;
        this.chatMessageDao = chatMessageDao;
        this.chatMessageConverter = chatMessageConverter;
    }

    @PostConstruct
    public void init() {
        this.mainGroup = this.createGroup(PUBLIC_MAIN_GROUP_NAME);
        mainGroup.setType(ChatGroupType.MAIN);
        mainGroup.setRoomId(PUBLIC_MAIN_ROOM_ID);

        groups.put(mainGroup.getRoomId(), mainGroup);
    }

    @Override
    public ChatUserDto addActiveUserToMain(ChatUserDto dto) {
        mainGroup.getUsers().add(dto);
        return dto;
    }

    @Override
    public void removeActiveUserFromMain(ChatUserDto dto) {
        mainGroup.getUsers().remove(dto);
    }

    @Override
    public ChatUserDto getUser(String sessionId) {
        return mainGroup.getUsers()
                .stream()
                .filter(user -> user.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ChatUserDto getUserByAccountId(Long id) {
        return mainGroup.getUsers()
                .stream()
                .filter(user -> id.equals(user.getAccountId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeFromAllGroup(ChatUserDto chatUserDto) {
        groups.values().removeIf(group -> {
            group.getUsers().removeIf(user -> user.getSessionId().equals(chatUserDto.getSessionId()));
            return group.getType() == ChatGroupType.PRIVATE && group.getUsers().isEmpty();
        });
    }

    @Override
    public ChatGroupDto createGroup(String name) {
        ChatGroupDto chatGroupDto = new ChatGroupDto();
        chatGroupDto.setName(name);
        chatGroupDto.setUsers(Collections.synchronizedList(new ArrayList<>()));

        return chatGroupDto;
    }

    @Override
    public void send(String roomId, ChatInfoDto chatInfoDto) {
        log.debug("To room: {} Info: {}", roomId, chatInfoDto);

        ChatInfoDto saveChatInfoDto = chatInfoService.save(chatInfoDto);

        simpMessagingTemplate.convertAndSend("/topic/info." + roomId, saveChatInfoDto);
    }

    @Override
    public void send(ChatInfoDto chatInfoDto) {
        for (ChatUserDto chatUserDto : mainGroup.getUsers()) {
            send(chatUserDto.getRoomName(), chatInfoDto);
        }
    }

    @Override
    public void sendDisconnect(String sessionId) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage("Bad");
        headerAccessor.setSessionId(sessionId);
        simpMessagingTemplate.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }

    @Override
    @Transactional
    public void send(ChatMessageDto message) {
        log.debug("messages: {}", message);

        ChatMessageEntity save = chatMessageDao.save(chatMessageConverter.convertToModel(message));
        message.setId(save.getId());

        simpMessagingTemplate.convertAndSend("/topic/chats." + message.getRoom(), message);
    }

    @Override
    @Transactional
    public ChatGroupDto createPrivateGroup(String name, IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должны быть ID пользователей");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        String roomId = createRoomId(accountByAuthentication.getId(), idsDto.getIds().get(0));

        ChatGroupDto group = groups.containsKey(roomId)
                ? groups.get(roomId)
                : createGroup(name);

        group.setType(ChatGroupType.PRIVATE);
        group.setRoomId(roomId);
        group.setUserCreated(accountByAuthentication);

        List<ChatUserDto> activeUserToGroup = mainGroup.getUsers()
                .stream()
                .filter(user -> idsDto.getIds().contains(user.getAccountId()) ||
                        user.getAccountId().equals(accountByAuthentication.getId()))
                .collect(Collectors.toList());
        group.getUsers().addAll(activeUserToGroup);

        /*if(StringUtils.isEmpty(group.getName())) {
            String groupName = accountDao.findById(idsDto.getIds())
                    .stream()
                    .map(this::accountsToChatGroupName)
                    .collect(Collectors.joining(","));

            group.setName(groupName);
        }*/

        groups.put(group.getRoomId(), group);

        ChatInfoDto chatInfoDto = new ChatInfoDto();
        chatInfoDto.setData(group.getRoomId());
        chatInfoDto.setAccountId(group.getUserCreated().getId());
        chatInfoDto.setAccount(group.getUserCreated());
        chatInfoDto.setCreateInfo(new Date());
        chatInfoDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);

        for (Long accountId : idsDto.getIds()) {
            chatInfoDto.setToAccountId(accountId);
            chatInfoDto.setId(null);
            send(chatInfoDto.getToAccountId().toString(), chatInfoDto);
        }

        return group;
    }

    private String accountsToChatGroupName(AccountEntity accountEntity) {
        return "[" + Optional.ofNullable(accountEntity.getChatName())
                        .filter(chatName -> !chatName.trim().isEmpty())
                        .orElse(accountEntity.getUsername()) +
                "]";
    }

    private String createRoomId(@NonNull Long fromAccountId, @NonNull Long toAccountId) {
        String roomId = key;
        if(fromAccountId < toAccountId) {
            roomId += fromAccountId.toString() + toAccountId.toString();
        } else {
            roomId += toAccountId.toString() + fromAccountId.toString();
        }

        return stringToHash(roomId);
    }

    private String stringToHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            return Hex.encodeHexString(resultByte);
        } catch (NoSuchAlgorithmException ignore) {
            return null;
        }
    }

    @Override
    public ChatGroupDto createPublicGroup(String name, IdsDto idsDto) {
        ChatGroupDto group = createGroup(name);
        group.setType(ChatGroupType.PUBLIC);
        group.setUserCreated(authenticationService.getAccountByAuthentication());
        return group;
    }

    @Override
    public ChatGroupDto getByRoomId(String roomId) {
        return groups.get(roomId);
    }

    @Override
    public List<ChatGroupDto> getAll() {
        return groups.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<ChatGroupDto> getAllForUser() {
        return groups.values()
                .stream()
                .filter(this::filterForUser)
                .collect(Collectors.toList());
    }

    private boolean filterForUser(ChatGroupDto groupDto) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        return groupDto.getType() == ChatGroupType.PUBLIC ||
                groupDto.getType() == ChatGroupType.MAIN ||
                accountByAuthentication.isAccessOit() ||
                (groupDto.getUserCreated() != null && groupDto.getUserCreated()
                        .getId().equals(accountByAuthentication.getId())) ||
                (groupDto.getUsers() != null &&
                        groupDto.getUsers()
                                .stream()
                                .anyMatch(chatUserDto -> chatUserDto.getAccountId().equals(accountByAuthentication.getId())));
    }

    @Override
    public ChatGroupDto save(ChatGroupDto dto) {
        groups.put(dto.getRoomId(), dto);

        return dto;
    }

    @Override
    public List<ChatGroupDto> save(List<ChatGroupDto> dtos) {
        dtos.forEach(this::save);
        return dtos;
    }

    @Override
    public ChatGroupDto getById(Long id) {
        return null;
    }

    @Override
    public List<ChatGroupDto> getById(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public void delete(ChatGroupDto dto) {
        if(dto.getId() != null) {
            groups.remove(dto.getRoomId());
        }
    }

    @Override
    public void delete(List<ChatGroupDto> dtos) {
        dtos.forEach(this::delete);
    }

    @Override
    public Page<ChatGroupDto> getAll(Integer number, Integer pageSize) {
        return null;
    }

    @Override
    public ChatGroupDto getMainGroup() {
        return mainGroup;
    }

    @Override
    @Transactional
    public void hideMessage(IdsDto idsDto, boolean hide) {
        if(idsDto != null && idsDto.getIds() != null && !idsDto.getIds().isEmpty()) {
            AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
            List<ChatMessageEntity> hideMessages = chatMessageDao.findById(idsDto.getIds())
                    .stream()
                    .filter(message -> accountByAuthentication.isAccessOit() || message.getAccountId().equals(accountByAuthentication.getId()))
                    .collect(Collectors.toList());

            if(hideMessages.isEmpty()) {
                return;
            }

            chatMessageDao.setHideOnIds(hideMessages.stream()
                    .map(ChatMessageEntity::getId)
                    .collect(Collectors.toList()), hide);

            ChatInfoDto chatInfoDto = new ChatInfoDto();
            chatInfoDto.setData(idsDto.getIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setCommand(ChatInfoCommand.HIDE_MESSAGES);
            chatInfoDto.setAccount(accountByAuthentication);
            chatInfoDto.setAccountId(accountByAuthentication.getId());

            for (ChatMessageEntity chatMessageEntity : hideMessages) {
                ChatGroupDto chatGroupDto = groups.get(chatMessageEntity.getRoom());
                if(chatGroupDto != null && !chatGroupDto.getUsers().isEmpty()) {
                    for (ChatUserDto chatUserDto : chatGroupDto.getUsers()) {
                        chatInfoDto.setId(null);
                        send(chatUserDto.getRoomName(), chatInfoDto);
                    }
                }
            }
        }
    }
}

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
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
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
import java.util.function.Function;
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
    private final AccountConverter accountConverter;
    private final ChatMessageDao chatMessageDao;
    private final ChatMessageConverter chatMessageConverter;

    public ChatServiceImpl(@Value("${secret_key:secret_key}") String key,
                           ChatInfoService chatInfoService,
                           SimpMessagingTemplate simpMessagingTemplate,
                           AuthenticationService authenticationService,
                           AccountDao accountDao,
                           AccountConverter accountConverter,
                           ChatMessageDao chatMessageDao,
                           ChatMessageConverter chatMessageConverter) {
        this.key = key;
        this.chatInfoService = chatInfoService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authenticationService = authenticationService;
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
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
    public ChatUserDto addActiveUser(ChatUserDto dto) {
        for (ChatGroupDto chatGroupDto : groups.values()) {
            for (ChatUserDto chatUserDto : chatGroupDto.getUsers()) {
                if(chatUserDto.getAccountId().equals(dto.getAccountId()) &&
                        chatUserDto.getSessionId() == null) {
                    chatUserDto.setSessionId(dto.getSessionId());
                }
            }
        }

        mainGroup.getUsers().add(dto);

        return dto;
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
        for (ChatGroupDto chatGroupDto : groups.values()) {
            chatGroupDto.getUsers().remove(dto);
        }
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
        mainGroup.getUsers().removeIf(user -> chatUserDto.getSessionId().equals(user.getSessionId()));

        for (ChatGroupDto chatGroupDto : groups.values()) {
            for (ChatUserDto userDto : chatGroupDto.getUsers()) {
                if(Objects.equals(chatUserDto.getSessionId(), userDto.getSessionId())) {
                    userDto.setSessionId(null);
                }
            }
        }

        groups.values().removeIf(group -> group.getType() == ChatGroupType.PRIVATE &&
                (group.getUsers().isEmpty() || group.getUsers()
                        .stream()
                        .map(ChatUserDto::getSessionId)
                        .allMatch(StringUtils::isEmpty)));
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
        if(message.getType() != ChatMessageType.SERVER_INFO &&
                Optional.ofNullable(getByRoomId(message.getRoom()))
                        .map(ChatGroupDto::getType)
                        .orElse(ChatGroupType.PUBLIC) == ChatGroupType.MAIN &&
                !message.getFromAccount().isAccessWriteMainChat()) {
            throw new CustomHttpCodeException("Нет доступа для записи сообщения в этот чат", ErrorCode.FORBIDDEN);
        } else {
            log.debug("messages: {}", message);

            ChatMessageEntity save = chatMessageDao.save(chatMessageConverter.convertToModel(message));
            message.setId(save.getId());

            simpMessagingTemplate.convertAndSend("/topic/chats." + message.getRoom(), message);
        }
    }

    @Override
    @Transactional
    public ChatGroupDto createPrivateGroup(String name, IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должны быть ID пользователей");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        List<Long> allUserIds = idsDto.getIds().stream().distinct().collect(Collectors.toList());
        if(!allUserIds.contains(accountByAuthentication.getId())) {
            allUserIds.add(accountByAuthentication.getId());
        }

        String roomId = createRoomId(allUserIds);

        if(groups.containsKey(roomId)) {
            return groups.get(roomId);
        }

        ChatGroupDto group = createGroup(name);

        group.setType(ChatGroupType.PRIVATE);
        group.setRoomId(roomId);
        group.setUserCreated(accountByAuthentication);

        Map<Long, ChatUserDto> activeUserToGroup = mainGroup.getUsers()
                .stream()
                .filter(user -> allUserIds.contains(user.getAccountId()))
                .collect(Collectors.toMap(ChatUserDto::getAccountId, Function.identity()));

        List<Long> offlineIds = allUserIds
                .stream()
                .filter(userId -> !activeUserToGroup.containsKey(userId))
                .collect(Collectors.toList());

        group.getUsers().addAll(activeUserToGroup.values());

        if(!offlineIds.isEmpty()) {
            List<AccountDto> accountDtos = accountConverter
                    .convertToDto(accountDao.findById(offlineIds));

            List<ChatUserDto> chatUserDtoOffline = accountDtos
                    .stream()
                    .map(this::createChatUserDto)
                    .collect(Collectors.toList());

            group.getUsers().addAll(chatUserDtoOffline);
        }

        if(StringUtils.isEmpty(group.getName())) {
            String groupName = accountDao.findById(allUserIds)
                    .stream()
                    .map(this::accountsToChatGroupName)
                    .collect(Collectors.joining(","));

            group.setName(groupName);
        }

        groups.put(group.getRoomId(), group);

        ChatInfoDto chatInfoDto = new ChatInfoDto();
        chatInfoDto.setData(group.getRoomId());
        chatInfoDto.setAccountId(group.getUserCreated().getId());
        chatInfoDto.setAccount(group.getUserCreated());
        chatInfoDto.setCreateInfo(new Date());
        chatInfoDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);

        for (Long accountId : idsDto.getIds()) {
            if(!accountByAuthentication.getId().equals(accountId)) {
                chatInfoDto.setToAccountId(accountId);
                chatInfoDto.setId(null);
                send(chatInfoDto.getToAccountId().toString(), chatInfoDto);
            }
        }

        return group;
    }

    private String accountsToChatGroupName(AccountEntity accountEntity) {
        return "[" + Optional.ofNullable(accountEntity.getChatName())
                        .filter(chatName -> !chatName.trim().isEmpty())
                        .orElse(accountEntity.getUsername()) +
                "]";
    }

    private String createRoomId(@NonNull List<Long> accountIds) {
        String roomId = key + accountIds
                .stream()
                .sorted(Long::compareTo)
                .map(Object::toString)
                .collect(Collectors.joining(""));

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

        return groupDto.getType() == ChatGroupType.MAIN ||
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

    @Override
    public ChatUserDto createChatUserDto(AccountDto accountDto) {
        ChatUserDto chatUserDto = new ChatUserDto();

        String username = accountDto.getChatName();
        if(StringUtils.isEmpty(username)) {
            EmployeeDto employee = accountDto.getEmployee();
            if(employee != null) {
                username = employee.getInitials();
            } else {
                username = accountDto.getUsername();
            }
        }

        chatUserDto.setName(username);
        chatUserDto.setAccountId(accountDto.getId());
        chatUserDto.setAccount(accountDto);
        chatUserDto.setRoomName(accountDto.getId().toString());
        return chatUserDto;
    }
}

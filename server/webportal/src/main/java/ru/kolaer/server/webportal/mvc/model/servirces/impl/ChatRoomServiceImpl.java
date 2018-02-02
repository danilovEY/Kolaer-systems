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
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.converter.ChatMessageConverter;
import ru.kolaer.server.webportal.mvc.model.converter.ChatRoomConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatRoomDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatRoomEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatRoomService;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
@Slf4j
public class ChatRoomServiceImpl extends AbstractDefaultService<ChatRoomDto, ChatRoomEntity, ChatRoomDao, ChatRoomConverter>
        implements ChatRoomService {
    private final String PUBLIC_MAIN_ROOM_NAME = "КолАЭР";
    private final String PUBLIC_MAIN_ROOM_KEY = "main";
    private ChatRoomDto mainGroup;

    private final String key;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthenticationService authenticationService;
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;
    private final ChatMessageDao chatMessageDao;
    private final ChatMessageConverter chatMessageConverter;

    public ChatRoomServiceImpl(@Value("${secret_key:secret_key}") String key,
                               SimpMessagingTemplate simpMessagingTemplate,
                               AuthenticationService authenticationService,
                               AccountDao accountDao,
                               AccountConverter accountConverter,
                               ChatMessageDao chatMessageDao,
                               ChatMessageConverter chatMessageConverter,
                               ChatRoomDao defaultEntityDao,
                               ChatRoomConverter converter) {
        super(defaultEntityDao, converter);
        this.key = key;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authenticationService = authenticationService;
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
        this.chatMessageDao = chatMessageDao;
        this.chatMessageConverter = chatMessageConverter;
    }

    @PostConstruct
    public void init() {
        mainGroup = createGroup(PUBLIC_MAIN_ROOM_NAME);
        mainGroup.setId(0L);
        mainGroup.setRoomKey(PUBLIC_MAIN_ROOM_KEY);
        mainGroup.setType(ChatGroupType.MAIN);
    }

    @Override
    public ChatUserDto addActiveUser(ChatUserDto dto) {
        mainGroup.getUsers().add(dto);

        return dto;
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
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
    public ChatRoomDto createGroup(String name) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setName(name);
        chatRoomDto.setUsers(Collections.synchronizedList(new ArrayList<>()));

        return chatRoomDto;
    }

    @Override
    public void send(String roomId, ChatInfoDto chatInfoDto) {
        log.debug("To room: {} Info: {}", roomId, chatInfoDto);

        simpMessagingTemplate.convertAndSend("/topic/info." + roomId, chatInfoDto);
    }

    @Override
    public void send(ChatInfoDto chatInfoDto) {
        for (ChatUserDto chatUserDto : mainGroup.getUsers()) {
            send(chatUserDto.getAccountId().toString(), chatInfoDto);
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
                Optional.ofNullable(defaultEntityDao.findById(message.getRoomId()))
                        .map(ChatRoomEntity::getType)
                        .orElse(ChatGroupType.PUBLIC) == ChatGroupType.MAIN &&
                !message.getFromAccount().isAccessWriteMainChat()) {
            throw new CustomHttpCodeException("Нет доступа для записи сообщения в этот чат", ErrorCode.FORBIDDEN);
        } else {
            log.debug("messages: {}", message);

            ChatMessageEntity save = chatMessageDao.save(chatMessageConverter.convertToModel(message));
            message.setId(save.getId());

            simpMessagingTemplate.convertAndSend("/topic/chats." + message.getRoomId(), message);
        }
    }

    @Override
    public ChatRoomDto createSingleGroup(IdDto idDto) {
        if(idDto == null || idDto.getId() == null) {
            throw new UnexpectedRequestParams("Должен быть ID пользователя");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        List<Long> allUserIds = Arrays.asList(idDto.getId(), accountByAuthentication.getId());

        String roomKey = generateRoomKey(allUserIds);

        ChatRoomDto group = getByRoomKey(roomKey);
        if(group != null) {
            return group;
        }

        List<ChatUserDto> chatUserDtos = accountConverter.convertToDto(accountDao.findById(allUserIds))
                .stream()
                .map(this::createChatUserDto)
                .collect(Collectors.toList());

        group = createGroup(null);
        group.setType(ChatGroupType.SINGLE);
        group.setRoomKey(roomKey);
        group.setUserCreated(createChatUserDto(accountByAuthentication));
        group.getUsers().addAll(chatUserDtos);
        group = this.save(group);

        ChatInfoCreateNewRoomDto chatInfoDto = new ChatInfoCreateNewRoomDto();
        chatInfoDto.setData(group);
        chatInfoDto.setAccountId(accountByAuthentication.getId());
        chatInfoDto.setCreateInfo(new Date());

        for (Long accId : allUserIds) {
            send(accId.toString(), chatInfoDto);
        }

        return group;
    }

    private ChatRoomDto getByRoomKey(String roomKey) {
        return defaultConverter.convertToDto(defaultEntityDao.findByRoomKey(roomKey));
    }

    @Override
    @Transactional
    public ChatRoomDto createPrivateGroup(String name, IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должны быть ID пользователей");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        List<Long> allUserIds = idsDto.getIds().stream().distinct().collect(Collectors.toList());
        if(!allUserIds.contains(accountByAuthentication.getId())) {
            allUserIds.add(accountByAuthentication.getId());
        }

        List<ChatUserDto> chatUserDtos = accountConverter.convertToDto(accountDao.findById(allUserIds))
                .stream()
                .map(this::createChatUserDto)
                .collect(Collectors.toList());

        ChatRoomDto group = createGroup(name);
        group.setType(ChatGroupType.PRIVATE);
        group.setUserCreated(createChatUserDto(accountByAuthentication));
        group.getUsers().addAll(chatUserDtos);
        group = this.save(group);

        ChatInfoCreateNewRoomDto chatInfoDto = new ChatInfoCreateNewRoomDto();
        chatInfoDto.setData(group);
        chatInfoDto.setAccountId(accountByAuthentication.getId());
        chatInfoDto.setCreateInfo(new Date());

        for (Long accountId : allUserIds) {
            send(accountId.toString(), chatInfoDto);
        }

        return group;
    }

    private String accountsToChatGroupName(AccountEntity accountEntity) {
        return "[" + Optional.ofNullable(accountEntity.getChatName())
                        .filter(chatName -> !chatName.trim().isEmpty())
                        .orElse(accountEntity.getUsername()) +
                "]";
    }

    private String generateRoomKey(@NonNull List<Long> accountIds) {
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
    public ChatRoomDto createPublicGroup(String name, IdsDto idsDto) {
        ChatRoomDto group = createGroup(name);
        group.setType(ChatGroupType.PUBLIC);
        group.setUserCreated(createChatUserDto(authenticationService.getAccountByAuthentication()));
        return group;
    }

    @Override
    public List<ChatRoomDto> getAll() {
        return Collections.singletonList(mainGroup);
    }

    @Override
    public List<ChatRoomDto> getAllForUser() {
        return Stream.of(mainGroup)
                .filter(this::filterForUser)
                .collect(Collectors.toList());
    }

    private boolean filterForUser(ChatRoomDto groupDto) {
        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

        return groupDto.getType() == ChatGroupType.MAIN ||
                //accountByAuthentication.isAccessOit() ||
                (groupDto.getUserCreated() != null && groupDto.getUserCreated()
                        .getId().equals(accountByAuthentication.getId())) ||
                (groupDto.getUsers() != null &&
                        groupDto.getUsers()
                                .stream()
                                .anyMatch(chatUserDto -> chatUserDto.getAccountId().equals(accountByAuthentication.getId())));
    }

    @Override
    public List<ChatRoomDto> save(List<ChatRoomDto> dtos) {
        dtos.forEach(this::save);
        return dtos;
    }

    @Override
    public ChatRoomDto getById(Long id) {
        return null;
    }

    @Override
    public List<ChatRoomDto> getById(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public void delete(ChatRoomDto dto) {
        if(dto.getId() != null) {

        }
    }

    @Override
    public void delete(List<ChatRoomDto> dtos) {
        dtos.forEach(this::delete);
    }

    @Override
    public Page<ChatRoomDto> getAll(Integer number, Integer pageSize) {
        return null;
    }

    @Override
    public ChatRoomDto getMainGroup() {
        return mainGroup;
    }

    @Override
    @Transactional
    public void hideMessage(IdsDto idsDto, boolean hide) {
//        if(idsDto != null && idsDto.getIds() != null && !idsDto.getIds().isEmpty()) {
//            AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
//            List<ChatMessageEntity> hideMessages = chatMessageDao.findById(idsDto.getIds())
//                    .stream()
//                    .filter(message -> accountByAuthentication.isAccessOit() || message.getAccountId().equals(accountByAuthentication.getId()))
//                    .collect(Collectors.toList());
//
//            if(hideMessages.isEmpty()) {
//                return;
//            }
//
//            chatMessageDao.setHideOnIds(hideMessages.stream()
//                    .map(ChatMessageEntity::getId)
//                    .collect(Collectors.toList()), hide);
//
//            ChatInfoDto chatInfoDto = new ChatInfoDto();
//            chatInfoDto.setData(idsDto.getIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
//            chatInfoDto.setCreateInfo(new Date());
//            chatInfoDto.setCommand(ChatInfoCommand.HIDE_MESSAGES);
//            chatInfoDto.setAccount(accountByAuthentication);
//            chatInfoDto.setAccountId(accountByAuthentication.getId());
//
//            for (ChatMessageEntity chatMessageEntity : hideMessages) {
//                ChatRoomDto chatRoomDto = groups.get(chatMessageEntity.getRoom());
//                if(chatRoomDto != null && !chatRoomDto.getUsers().isEmpty()) {
//                    for (ChatUserDto chatUserDto : chatRoomDto.getUsers()) {
//                        chatInfoDto.setId(null);
//                        send(chatUserDto.getRoomName(), chatInfoDto);
//                    }
//                }
//            }
//        }
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
        return chatUserDto;
    }
}

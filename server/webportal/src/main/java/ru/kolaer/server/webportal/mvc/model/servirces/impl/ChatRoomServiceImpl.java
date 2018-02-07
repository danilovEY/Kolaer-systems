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
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatRoomService;

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
public class ChatRoomServiceImpl extends AbstractDefaultService<ChatRoomDto, ChatRoomEntity, ChatRoomDao, ChatRoomConverter>
        implements ChatRoomService {
    private final Map<Long, ChatUserDto> activeUser = new HashMap<>();
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

    @Override
    public ChatUserDto addActiveUser(ChatUserDto dto) {
        return activeUser.put(dto.getAccountId(), dto);
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
        activeUser.remove(dto.getAccountId());
    }

    @Override
    public ChatUserDto getUser(String sessionId) {
        return activeUser.values()
                .stream()
                .filter(user -> user.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ChatUserDto getUserByAccountId(Long id) {
        return activeUser.get(id);
    }

    @Override
    public ChatRoomDto createGroup(String name) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setName(name);
        chatRoomDto.setUsers(new ArrayList<>());

        return chatRoomDto;
    }

    @Override
    public void send(ChatInfoUserActionDto chatInfoUserActionDto) {
        for (ChatUserDto chatUserDto : activeUser.values()) {
            String roomId = chatUserDto.getAccountId().toString();
            simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_INFO_USER_ACTION + roomId, chatInfoUserActionDto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getAllRoomForAuthUser() {
        List<ChatRoomEntity> allByUserInRoom = defaultEntityDao.findAllByUserInRoom(authenticationService.getAccountByAuthentication().getId());

        if(allByUserInRoom.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChatRoomDto> rooms = new ArrayList<>();

        for (ChatRoomEntity chatRoomEntity : allByUserInRoom) {
            ChatRoomDto chatRoomDto = defaultConverter.convertToDto(chatRoomEntity);
            chatRoomDto.setUsers(getUsersByIds(chatRoomEntity.getAccountIds()));

            rooms.add(chatRoomDto);
        }

        return rooms;
    }

    @Override
    public void send(ChatInfoRoomActionDto chatInfoRoomActionDto) {
        for (ChatUserDto chatUserDto : chatInfoRoomActionDto.getChatRoomDto().getUsers()) {
            String roomId = chatUserDto.getAccountId().toString();
            simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_INFO_ROOM_ACTION + roomId, chatInfoRoomActionDto);
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
                        .orElse(ChatGroupType.PUBLIC) == ChatGroupType.MAIN) {
            throw new CustomHttpCodeException("Нет доступа для записи сообщения в этот чат", ErrorCode.FORBIDDEN);
        } else {
            log.debug("messages: {}", message);

            ChatMessageEntity save = chatMessageDao.save(chatMessageConverter.convertToModel(message));
            message.setId(save.getId());

            simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_CHAT_MESSAGE + message.getRoomId(), message);
        }
    }

    @Override
    @Transactional
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

        group = createGroup(null);
        group.setType(ChatGroupType.SINGLE);
        group.setRoomKey(roomKey);
        group.setUserCreated(createChatUserDto(accountByAuthentication));
        group.getUsers().addAll(getUsersByIds(allUserIds));
        group = this.save(group);

        ChatInfoRoomActionDto chatInfoCreateNewRoomDto = new ChatInfoRoomActionDto();
        chatInfoCreateNewRoomDto.setChatRoomDto(group);
        chatInfoCreateNewRoomDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);
        chatInfoCreateNewRoomDto.setFromAccount(accountByAuthentication.getId());
        chatInfoCreateNewRoomDto.setCreateInfo(new Date());

        send(chatInfoCreateNewRoomDto);

        return group;
    }

    @Override
    @Transactional
    public List<ChatRoomDto> createSingleGroup(IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должены быть ID пользователей");
        }

        List<ChatRoomDto> roomDtos = new ArrayList<>(idsDto.getIds().size());

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        ChatUserDto authChatUserDto = createChatUserDto(accountByAuthentication);

        for (Long accountId : idsDto.getIds()) {
            List<Long> allUserIds = Arrays.asList(accountId, accountByAuthentication.getId());

            String roomKey = generateRoomKey(allUserIds);

            ChatRoomDto group = getByRoomKey(roomKey);
            if(group == null) {
                group = createGroup(null);
                group.setType(ChatGroupType.SINGLE);
                group.setRoomKey(roomKey);
                group.setUserCreated(authChatUserDto);
                group.getUsers().addAll(getUsersByIds(allUserIds));
                group = this.save(group);

                ChatInfoRoomActionDto chatInfoCreateNewRoomDto = new ChatInfoRoomActionDto();
                chatInfoCreateNewRoomDto.setChatRoomDto(group);
                chatInfoCreateNewRoomDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);
                chatInfoCreateNewRoomDto.setFromAccount(accountByAuthentication.getId());
                chatInfoCreateNewRoomDto.setCreateInfo(new Date());

                send(chatInfoCreateNewRoomDto);
            }

            roomDtos.add(group);
        }

        return roomDtos;
    }

    @Transactional(readOnly = true)
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

        ChatRoomDto group = createGroup(name);
        group.setType(ChatGroupType.PRIVATE);
        group.setUserCreated(createChatUserDto(accountByAuthentication));
        group.getUsers().addAll(getUsersByIds(allUserIds));
        group = this.save(group);

        ChatInfoRoomActionDto chatInfoCreateNewRoomDto = new ChatInfoRoomActionDto();
        chatInfoCreateNewRoomDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);
        chatInfoCreateNewRoomDto.setChatRoomDto(group);
        chatInfoCreateNewRoomDto.setFromAccount(accountByAuthentication.getId());
        chatInfoCreateNewRoomDto.setCreateInfo(new Date());

        send(chatInfoCreateNewRoomDto);

        return group;
    }

    private List<ChatUserDto> getUsersByIds(List<Long> accountIds) {
        if(CollectionUtils.isEmpty(accountIds)) {
            return Collections.emptyList();
        }

        List<Long> selectedIds = new ArrayList<>();
        List<ChatUserDto> users = new ArrayList<>();

        for (Long accountId : accountIds) {
            ChatUserDto chatUserDto = activeUser.get(accountId);
            if(chatUserDto != null) {
                users.add(chatUserDto);
            } else {
                selectedIds.add(accountId);
            }
        }

        if(!CollectionUtils.isEmpty(selectedIds)) {
            List<ChatUserDto> chatUserDtos = accountConverter.convertToDto(accountDao.findById(selectedIds))
                    .stream()
                    .map(this::createChatUserDto)
                    .collect(Collectors.toList());
            users.addAll(chatUserDtos);
        }

        return users;
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
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getAll() {
        return defaultConverter.convertToDto(defaultEntityDao.findAll());
    }

    @Override
    @Transactional
    public List<ChatRoomDto> save(List<ChatRoomDto> dtos) {
        dtos.forEach(this::save);
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoomDto getById(Long id) {
        return defaultConverter.convertToDto(defaultEntityDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getById(List<Long> ids) {
        return defaultConverter.convertToDto(defaultEntityDao.findById(ids));
    }

    @Override
    public void delete(ChatRoomDto dto) {

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

        chatUserDto.setStatus(ChatUserStatus.OFFLINE);
        chatUserDto.setName(username);
        chatUserDto.setAccountId(accountDto.getId());
        return chatUserDto;
    }

    @Override
    public Collection<ChatUserDto> getOnlineUsers() {
        return activeUser.values();
    }
}

package ru.kolaer.server.webportal.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.IdDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.*;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.model.converter.ChatRoomConverter;
import ru.kolaer.server.webportal.model.dao.AccountDao;
import ru.kolaer.server.webportal.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.model.dao.ChatRoomDao;
import ru.kolaer.server.webportal.model.entity.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.model.entity.chat.ChatRoomEntity;
import ru.kolaer.server.webportal.model.entity.general.AccountEntity;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;
import ru.kolaer.server.webportal.service.AuthenticationService;
import ru.kolaer.server.webportal.service.ChatRoomService;

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
public class ChatRoomServiceImpl extends AbstractDefaultService<ChatRoomDto, ChatRoomEntity, ChatRoomDao, ChatRoomConverter>
        implements ChatRoomService {
    private final Map<Long, ChatUserDto> activeUser = new HashMap<>();

    private final String key;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthenticationService authenticationService;
    private final AccountDao accountDao;
    private final ChatMessageDao chatMessageDao;
    private final SessionFactory sessionFactory;

    public ChatRoomServiceImpl(@Value("${secret_key:secret_key}") String key,
                               SimpMessagingTemplate simpMessagingTemplate,
                               AuthenticationService authenticationService,
                               AccountDao accountDao,
                               ChatMessageDao chatMessageDao,
                               ChatRoomDao defaultEntityDao,
                               ChatRoomConverter converter,
                               SessionFactory sessionFactory) {
        super(defaultEntityDao, converter);
        this.key = key;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authenticationService = authenticationService;
        this.accountDao = accountDao;
        this.chatMessageDao = chatMessageDao;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ChatUserDto addActiveUser(ChatUserDto dto) {
        activeUser.put(dto.getAccountId(), dto);

        return dto;
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
        dto.setStatus(ChatUserStatus.OFFLINE);
    }

    @Override
    public ChatUserDto getUser(String sessionId) {
        return activeUser.values()
                .stream()
                .filter(user -> sessionId.equals(user.getSessionId()))
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
            if(chatUserDto.getStatus() == ChatUserStatus.ONLINE) {
                String roomId = chatUserDto.getAccountId().toString();

                log.info("Send user action to: {}", roomId);

                simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_INFO_USER_ACTION + roomId, chatInfoUserActionDto);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getAllRoomForAuthUser() {
        List<ChatRoomEntity> allByUserInRoom = defaultEntityDao.findAllByUser(authenticationService.getAccountByAuthentication().getId());

        if(allByUserInRoom.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChatRoomDto> rooms = new ArrayList<>();

        List<Long> roomIds = allByUserInRoom.stream()
                .map(ChatRoomEntity::getId)
                .collect(Collectors.toList());

        Map<Long, List<Long>> allUsersByRooms = defaultEntityDao.findAllUsersByRooms(roomIds);

        List<Long> allUserIds = allUsersByRooms.values()
                .stream()
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, ChatUserDto> userDtoMap = getUsersByIds(allUserIds).stream()
                .collect(Collectors.toMap(ChatUserDto::getAccountId, Function.identity()));

        for (ChatRoomEntity chatRoomEntity : allByUserInRoom) {
            ChatRoomDto chatRoomDto = defaultConverter.convertToDto(chatRoomEntity);

            List<ChatUserDto> userInRoom = allUsersByRooms.get(chatRoomDto.getId()).stream()
                    .map(userDtoMap::get)
                    .collect(Collectors.toList());

            chatRoomDto.setUsers(userInRoom);

            rooms.add(chatRoomDto);
        }

        return rooms;
    }

    @Override
    @Transactional
    public void markReadMessages(IdsDto idsDto, boolean read) {
        if(idsDto != null && !CollectionUtils.isEmpty(idsDto.getIds())) {
            AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
            chatMessageDao.markRead(idsDto.getIds(), accountByAuthentication.getId(), read);
        }
    }

    @Override
    public void send(ChatInfoRoomActionDto chatInfoRoomActionDto) {
        ChatRoomDto chatRoomDto = chatInfoRoomActionDto.getChatRoomDto();

        for (ChatUserDto chatUserDto : chatRoomDto.getUsers()) {
            if(chatUserDto.getStatus() == ChatUserStatus.ONLINE) {
                String roomId = chatUserDto.getAccountId().toString();

                log.info("Send room key: {} action to: {}", chatRoomDto.getRoomKey(), roomId);

                simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_INFO_ROOM_ACTION + roomId, chatInfoRoomActionDto);
            }
        }
    }

    @Override
    public void send(ChatInfoMessageActionDto chatInfoRoomActionDto) {
        List<Long> roomIds = chatInfoRoomActionDto.getChatMessageDtoList()
                .stream()
                .map(ChatMessageDto::getRoomId)
                .collect(Collectors.toList());

        List<Long> userIds = defaultEntityDao.findAllUsersByRooms(roomIds).values()
                .stream()
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        for (Long userId : userIds) {
            String roomId = userId.toString();

            log.info("Send message action to: {}", roomId);

            simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_INFO_MESSAGE_ACTION + roomId, chatInfoRoomActionDto);
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

            message.setFromAccount(this.getOrCreateUserByAccountId(message.getFromAccount().getAccountId()));
            ChatMessageEntity save = chatMessageDao.save(convertToModel(message));
            message.setId(save.getId());


            simpMessagingTemplate.convertAndSend(ChatConstants.TOPIC_CHAT_MESSAGE + message.getRoomId(), message);
        }
    }

    private ChatMessageEntity convertToModel(ChatMessageDto dto) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setId(dto.getId());
        chatMessageEntity.setRoomId(dto.getRoomId());
        chatMessageEntity.setMessage(dto.getMessage());
        chatMessageEntity.setCreateMessage(dto.getCreateMessage());
        chatMessageEntity.setType(dto.getType());
        Optional.ofNullable(dto.getFromAccount())
                .map(ChatUserDto::getAccountId)
                .ifPresent(chatMessageEntity::setAccountId);

        chatMessageEntity.setReadIds(Collections.singletonList(chatMessageEntity.getAccountId()));

        return chatMessageEntity;
    }

    private ChatMessageDto convertToDto(ChatMessageEntity chatMessageEntity) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setId(chatMessageEntity.getId());
        chatMessageDto.setRoomId(chatMessageEntity.getRoomId());
        chatMessageDto.setMessage(chatMessageEntity.getMessage());
        chatMessageDto.setCreateMessage(chatMessageEntity.getCreateMessage());
        chatMessageDto.setHide(chatMessageEntity.isHide());
        chatMessageDto.setType(chatMessageEntity.getType());
        Optional.ofNullable(chatMessageEntity.getAccountId())
                .map(this::getOrCreateUserByAccountId)
                .ifPresent(chatMessageDto::setFromAccount);

        return chatMessageDto;
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
            }

            roomDtos.add(group);
        }

        Map<String, ChatRoomDto> persistRooms = roomDtos.stream()
                .filter(room -> room.getId() == null)
                .collect(Collectors.toMap(ChatRoomDto::getRoomKey, Function.identity()));

        List<ChatRoomDto> saveRooms = save(persistRooms.values().stream().collect(Collectors.toList()));

        for (ChatRoomDto saveRoom : saveRooms) {
            saveRoom.setUsers(persistRooms.get(saveRoom.getRoomKey()).getUsers());

            ChatInfoRoomActionDto chatInfoCreateNewRoomDto = new ChatInfoRoomActionDto();
            chatInfoCreateNewRoomDto.setChatRoomDto(saveRoom);
            chatInfoCreateNewRoomDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);
            chatInfoCreateNewRoomDto.setFromAccount(accountByAuthentication.getId());
            chatInfoCreateNewRoomDto.setCreateInfo(new Date());

            send(chatInfoCreateNewRoomDto);
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

        String roomKey = generateRoomKey(allUserIds);

        ChatRoomDto group = getByRoomKey(roomKey);
        if(group == null) {
            log.info("Create private room: {}", roomKey);

            group = createGroup(name);
            group.setRoomKey(roomKey);
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
        }

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
            Map<Long, ChatUserDto> notAddUsers = accountDao.findById(selectedIds)
                    .stream()
                    .map(this::createChatUserDto)
                    .collect(Collectors.toMap(ChatUserDto::getAccountId, Function.identity()));

            activeUser.putAll(notAddUsers);

            users.addAll(notAddUsers.values());
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
    @Transactional
    public void quitFromRooms(IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должны быть ID групп");
        }

        AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        Long authAccountId = accountByAuthentication.getId();

        Map<Long, List<Long>> allUsersByRooms = defaultEntityDao.findAllUsersByRooms(idsDto.getIds());

        List<Long> roomIdsWithAuthUser = allUsersByRooms.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(authAccountId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        if(!roomIdsWithAuthUser.isEmpty()) {
            defaultEntityDao.removeUserFromRooms(roomIdsWithAuthUser, authAccountId);

            for (ChatRoomDto room : getById(roomIdsWithAuthUser)) {
                List<Long> accountInRoom = allUsersByRooms.get(room.getId());

                room.setUsers(getUsersByIds(accountInRoom));

                ChatInfoRoomActionDto chatInfoCreateNewRoomDto = new ChatInfoRoomActionDto();
                chatInfoCreateNewRoomDto.setCommand(ChatInfoCommand.QUIT_FROM_ROOM);
                chatInfoCreateNewRoomDto.setChatRoomDto(room);
                chatInfoCreateNewRoomDto.setFromAccount(authAccountId);
                chatInfoCreateNewRoomDto.setCreateInfo(new Date());

                send(chatInfoCreateNewRoomDto);
            }
        }


    }

    @Override
    @Transactional
    public void hideMessage(IdsDto idsDto, boolean hide) {
        if(idsDto != null && !CollectionUtils.isEmpty(idsDto.getIds())) {
            AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
            List<ChatMessageEntity> hideMessages = chatMessageDao.findById(idsDto.getIds())
                    .stream()
                    .filter(message -> accountByAuthentication.isAccessOit() || message.getAccountId().equals(accountByAuthentication.getId()))
                    .collect(Collectors.toList());

            if(hideMessages.isEmpty()) {
                return;
            }

            chatMessageDao.markHideOnIds(hideMessages.stream()
                    .map(ChatMessageEntity::getId)
                    .collect(Collectors.toList()), hide);

            hideMessages.forEach(message -> message.setHide(hide));

            ChatInfoMessageActionDto chatInfoDto = new ChatInfoMessageActionDto();
            chatInfoDto.setChatMessageDtoList(hideMessages.stream().map(this::convertToDto).collect(Collectors.toList()));
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setCommand(ChatInfoCommand.HIDE_MESSAGES);
            chatInfoDto.setFromAccount(accountByAuthentication.getId());

            send(chatInfoDto);
        }
    }

    @Override
    @Transactional
    public void deleteMessage(IdsDto idsDto) {
        if(idsDto != null && !CollectionUtils.isEmpty(idsDto.getIds())) {
            AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();

            List<ChatMessageEntity> messages = chatMessageDao.findById(idsDto.getIds());
            List<ChatMessageEntity> removedMessages = messages
                    .stream()
                    .filter(message -> accountByAuthentication.isAccessOit() ||
                            message.getAccountId().equals(accountByAuthentication.getId()))
                    .collect(Collectors.toList());

            if (!removedMessages.isEmpty()) {
                chatMessageDao.delete(removedMessages);
            }

            ChatInfoMessageActionDto chatInfoDto = new ChatInfoMessageActionDto();
            chatInfoDto.setChatMessageDtoList(removedMessages.stream().map(this::convertToDto).collect(Collectors.toList()));
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setCommand(ChatInfoCommand.DELETE_MESSAGES);
            chatInfoDto.setFromAccount(accountByAuthentication.getId());

            send(chatInfoDto);
        }
    }

    private ChatUserDto createChatUserDto(AccountEntity accountEntity) {
        ChatUserDto chatUserDto = new ChatUserDto();

        String username = accountEntity.getChatName();
        if(StringUtils.isEmpty(username)) {
            EmployeeEntity employee = accountEntity.getEmployee();
            if(employee != null) {
                username = employee.getInitials();
            } else {
                username = accountEntity.getUsername();
            }
        }

        chatUserDto.setStatus(ChatUserStatus.OFFLINE);
        chatUserDto.setName(username);
        chatUserDto.setAccountId(accountEntity.getId());
        return chatUserDto;
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

    @Override
    public ChatUserDto getOrCreateUserByAccountId(Long accountId) {
        return getUsersByIds(Collections.singletonList(accountId)).get(0);
    }

    @Override
    public List<ChatUserDto> getOrCreateUserByAccountId(List<Long> accountIds) {
        return getUsersByIds(accountIds);
    }
}

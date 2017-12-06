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
import org.springframework.util.CollectionUtils;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatInfoService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;
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
    private final ChatMessageService chatMessageService;
    private final ChatInfoService chatInfoService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthenticationService authenticationService;

    public ChatServiceImpl(@Value("${secret_key:secret_key}") String key,
                           ChatMessageService chatMessageService,
                           ChatInfoService chatInfoService,
                           SimpMessagingTemplate simpMessagingTemplate,
                           AuthenticationService authenticationService) {
        this.key = key;
        this.chatMessageService = chatMessageService;
        this.chatInfoService = chatInfoService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authenticationService = authenticationService;
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
        for (ChatGroupDto chatGroupDto : groups.values()) {
            chatGroupDto.getUsers().removeIf(user -> user.getSessionId().equals(chatUserDto.getSessionId()));
        }
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
    public void send(ChatMessageDto message) {
        log.debug("messages: {}", message);

        simpMessagingTemplate.convertAndSend("/topic/chats." + message.getRoom(), chatMessageService.save(message));
    }

    @Override
    public ChatGroupDto createPrivateGroup(String name, IdsDto idsDto) {
        if(idsDto == null || CollectionUtils.isEmpty(idsDto.getIds())) {
            throw new UnexpectedRequestParams("Должны быть ID пользователей");
        }

        String roomId = idsDto.getIds().size() > 1
                ? UUID.randomUUID().toString()
                : createRoomId(authenticationService.getAccountByAuthentication().getId(), idsDto.getIds().get(0));

        ChatGroupDto group = groups.containsKey(roomId)
                ? groups.get(roomId)
                : createGroup(name);

        group.setType(ChatGroupType.PRIVATE);
        group.setRoomId(roomId);
        group.setUserCreated(authenticationService.getAccountByAuthentication());

        groups.put(group.getRoomId(), group);

        ChatInfoDto chatInfoDto = new ChatInfoDto();
        chatInfoDto.setData(group.getRoomId());
        chatInfoDto.setAccountId(group.getUserCreated().getId());
        chatInfoDto.setAccount(group.getUserCreated());
        chatInfoDto.setCreateInfo(new Date());
        chatInfoDto.setCommand(ChatInfoCommand.CREATE_NEW_ROOM);

        for (Long accountId : idsDto.getIds()) {
            chatInfoDto.setToAccountId(accountId);
            send(chatInfoDto.getToAccountId().toString(), chatInfoDto);
        }

        return group;
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
}

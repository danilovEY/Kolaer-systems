package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
public class ChatServiceSimple implements ChatService {
    private final String PUBLIC_MAIN_GROUP_NAME = "Main";
    private final Map<String, ChatGroupDto> publicGroup = new HashMap<>();
    private final Map<String, ChatGroupDto> privateGroup = new HashMap<>();

    private final AuthenticationService authenticationService;

    public ChatServiceSimple(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostConstruct
    public void init() {
        ChatGroupDto mainGroup = this.createPrivateGroup(PUBLIC_MAIN_GROUP_NAME);
        publicGroup.put(mainGroup.getName(), mainGroup);
    }

    @Override
    public ChatUserDto addActiveUserToMain(ChatUserDto dto) {
        publicGroup.get(PUBLIC_MAIN_GROUP_NAME).getUsers().add(dto);
        return dto;
    }

    @Override
    public void removeActiveUserFromMain(ChatUserDto dto) {
        publicGroup.get(PUBLIC_MAIN_GROUP_NAME).getUsers().remove(dto);
    }

    @Override
    public ChatUserDto getUser(String sessionId) {
        return publicGroup.get(PUBLIC_MAIN_GROUP_NAME).getUsers()
                .stream()
                .filter(user -> user.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ChatUserDto getUserByAccountId(Long id) {
        return publicGroup.get(PUBLIC_MAIN_GROUP_NAME).getUsers()
                .stream()
                .filter(user -> id.equals(user.getAccountId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ChatGroupDto getOrCreatePrivateGroup(String name) {
        return privateGroup.computeIfAbsent(name, this::createPrivateGroup);
    }

    @Override
    public ChatGroupDto getOrCreatePublicGroup(String name) {
        return publicGroup.computeIfAbsent(name, this::createPublicGroup);
    }

    @Override
    public void removeFromAllGroup(ChatUserDto chatUserDto) {
        for (ChatGroupDto chatGroupDto : publicGroup.values()) {
            chatGroupDto.getUsers().remove(chatUserDto);
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
    public ChatGroupDto getGroup(String name) {
        return privateGroup.getOrDefault(name, publicGroup.get(name));
    }

    @Override
    public ChatGroupDto getOrCreatePrivateGroupByAccountId(IdsDto idsDto) {
        for (ChatGroupDto chatGroupDto : privateGroup.values()) {
            if(chatGroupDto.getUsers()
                    .stream()
                    .map(ChatUserDto::getAccountId)
                    .allMatch(idsDto.getIds()::contains)){
                return chatGroupDto;
            }
        }

        return createPrivateGroup(UUID.randomUUID().toString());
    }

    private ChatGroupDto createPrivateGroup(String name) {
        ChatGroupDto chatGroupDto = createGroup(name);
        chatGroupDto.setType(ChatGroupType.PRIVATE);
        chatGroupDto.setUserCreated(authenticationService.getAccountByAuthentication());
        return chatGroupDto;
    }


    private ChatGroupDto createPublicGroup(String name) {
        ChatGroupDto chatGroupDto = createGroup(name);
        chatGroupDto.setType(ChatGroupType.PUBLIC);
        chatGroupDto.setUserCreated(authenticationService.getAccountByAuthentication());
        return chatGroupDto;
    }

    @Override
    public List<ChatGroupDto> getAll() {
        return Stream.concat(publicGroup.values().stream(), privateGroup.values().stream())
                .collect(Collectors.toList());
    }

    @Override
    public ChatGroupDto save(ChatGroupDto dto) {
        if(dto.getType() == null || dto.getType() == ChatGroupType.PRIVATE) {
            privateGroup.put(dto.getName(), dto);
        } else {
            publicGroup.put(dto.getName(), dto);
        }

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
        return null;
    }

    @Override
    public void delete(ChatGroupDto dto) {

    }

    @Override
    public void delete(List<ChatGroupDto> dtos) {

    }

    @Override
    public Page<ChatGroupDto> getAll(Integer number, Integer pageSize) {
        return null;
    }
}

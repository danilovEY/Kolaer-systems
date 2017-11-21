package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
public class ChatServiceSimple implements ChatService {
    private final Map<String, ChatUserDto> activeUserDtoMap = Collections.synchronizedMap(new HashMap<>());

    private final ChatGroupDto mainGroup = new ChatGroupDto();
    private final List<ChatUserDto> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        mainGroup.setName("Main");
        mainGroup.setUsers(users);
    }

    @Override
    public ChatUserDto addActiveUser(ChatUserDto dto) {
        users.add(dto);
        activeUserDtoMap.put(dto.getSessionId(), dto);
        return dto;
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
        users.remove(dto);
        activeUserDtoMap.remove(dto.getSessionId());
    }

    @Override
    public boolean containsUser(String sessionId) {
        return activeUserDtoMap.containsKey(sessionId);
    }

    @Override
    public ChatUserDto getUser(String sessionId) {
        return activeUserDtoMap.get(sessionId);
    }

    @Override
    public ChatUserDto getUserByAccountId(Long id) {
        return users.stream()
                .filter(user -> id.equals(user.getAccountId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ChatGroupDto> getAll() {
        return Collections.singletonList(mainGroup);
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
    public ChatGroupDto save(ChatGroupDto dto) {
        return null;
    }

    @Override
    public List<ChatGroupDto> save(List<ChatGroupDto> dtos) {
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

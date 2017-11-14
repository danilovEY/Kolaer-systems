package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
public class ChatServiceSimple implements ChatService {
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
        return dto;
    }

    @Override
    public void removeActiveUser(ChatUserDto dto) {
        users.remove(dto);
    }

    @Override
    public ChatGroupDto getMainGroup() {
        return mainGroup;
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

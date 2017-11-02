package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ActiveUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.ActiveUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
@Service
public class ActiveUserServiceSimple implements ActiveUserService {
    private final List<ActiveUserDto> users = new ArrayList<>();

    @Override
    public List<ActiveUserDto> getAll() {
        return users;
    }

    @Override
    public ActiveUserDto getById(Long id) {
        return null;
    }

    @Override
    public List<ActiveUserDto> getById(List<Long> ids) {
        return null;
    }

    @Override
    public ActiveUserDto save(ActiveUserDto dto) {
        users.add(dto);
        return dto;
    }

    @Override
    public List<ActiveUserDto> save(List<ActiveUserDto> dtos) {
        return null;
    }

    @Override
    public void delete(ActiveUserDto dto) {
        users.remove(dto);
    }

    @Override
    public void delete(List<ActiveUserDto> dtos) {

    }

    @Override
    public Page<ActiveUserDto> getAll(Integer number, Integer pageSize) {
        return null;
    }
}

package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.security.RoleUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class UrlSecurityConverterImpl implements UrlSecurityConverter {
    private static final String SPLIT_ACCESS_SYMBOL = ",";

    @Override
    public UrlSecurityEntity convertToModel(UrlSecurityDto dto) {
        if(dto == null) {
            return null;
        }

        UrlSecurityEntity entity = new UrlSecurityEntity();
        entity.setId(dto.getId());
        entity.setAccessAll(dto.isAccessAll());
        entity.setAccessOit(dto.isAccessOit());
        entity.setAccessUser(dto.isAccessUser());
        entity.setDescription(dto.getDescription());
        entity.setUrl(dto.getUrl());
        entity.setRequestMethod(dto.getRequestMethod());
        return entity;
    }

    @Override
    public UrlSecurityDto convertToDto(UrlSecurityEntity model) {
        if(model == null) {
            return null;
        }

        UrlSecurityDto dto = new UrlSecurityDto();
        dto.setId(model.getId());
        dto.setRequestMethod(model.getRequestMethod());
        dto.setDescription(model.getDescription());
        dto.setUrl(model.getUrl());
        dto.setAccessAll(model.isAccessAll());
        dto.setAccessOit(model.isAccessOit());
        dto.setAccessUser(model.isAccessUser());
        return dto;
    }

    @Override
    public UrlSecurityDto updateData(UrlSecurityDto oldDto, UrlSecurityEntity newModel) {
        if(oldDto == null || newModel == null) {
            return null;
        }
        oldDto.setId(newModel.getId());
        oldDto.setRequestMethod(newModel.getRequestMethod());
        oldDto.setDescription(newModel.getDescription());
        oldDto.setUrl(newModel.getUrl());
        oldDto.setAccessAll(newModel.isAccessAll());
        oldDto.setAccessOit(newModel.isAccessOit());
        oldDto.setAccessUser(newModel.isAccessUser());
        return oldDto;
    }

    @Override
    public List<String> convertToAccesses(UrlSecurityDto urlPath) {
        return Optional.ofNullable(urlPath)
                .map(RoleUtils::roleToListString)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<String> convertToAccesses(UrlSecurityEntity urlPath) {
        return Optional.ofNullable(urlPath)
                .map(RoleUtils::roleToListString)
                .orElse(Collections.emptyList());
    }

}

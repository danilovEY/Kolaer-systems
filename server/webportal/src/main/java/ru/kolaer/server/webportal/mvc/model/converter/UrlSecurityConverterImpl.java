package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class UrlSecurityConverterImpl implements UrlSecurityConverter {
    private static final String SPLIT_ACCESS_SYMBOL = ",";

    @Override
    public UrlSecurityEntity convertToModel(UrlSecurityDto dto) {
        UrlSecurityEntity entity = new UrlSecurityEntity();
        entity.setId(dto.getId());
        entity.setAccess(dto.getAccess());
        entity.setDescription(dto.getDescription());
        entity.setRequestMethod(dto.getRequestMethod());
        return entity;
    }

    @Override
    public UrlSecurityDto convertToDto(UrlSecurityEntity model) {
        UrlSecurityDto dto = new UrlSecurityDto();
        dto.setId(model.getId());
        dto.setRequestMethod(model.getRequestMethod());
        dto.setDescription(model.getDescription());
        dto.setAccess(model.getAccess());
        return dto;
    }

    @Override
    public UrlSecurityDto updateData(UrlSecurityDto oldDto, UrlSecurityEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setRequestMethod(newModel.getRequestMethod());
        oldDto.setDescription(newModel.getDescription());
        oldDto.setAccess(newModel.getAccess());
        return oldDto;
    }

    @Override
    public List<String> convertToAccesses(UrlSecurityDto urlPath) {
        return Optional.ofNullable(urlPath)
                .map(UrlSecurityDto::getAccess)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<String> convertToAccesses(UrlSecurityEntity urlPath) {
        return Optional.ofNullable(urlPath)
                .map(UrlSecurityEntity::getAccess)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    @Override
    public String convertToAccess(List<String> assesses) {
        if(assesses == null || assesses.isEmpty()) {
            return null;
        }
        return assesses.stream().collect(Collectors.joining(SPLIT_ACCESS_SYMBOL));
    }

}

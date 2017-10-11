package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 11.10.2017.
 */
@Component
public class PasswordRepositoryConverterImpl implements PasswordRepositoryConverter {
    private final PasswordHistoryConverter passwordHistoryConverter;

    public PasswordRepositoryConverterImpl(PasswordHistoryConverter passwordHistoryConverter) {
        this.passwordHistoryConverter = passwordHistoryConverter;
    }

    @Override
    public PasswordRepositoryEntity convertToModel(PasswordRepositoryDto dto) {
        PasswordRepositoryEntity passwordRepositoryEntity = new PasswordRepositoryEntity();
        passwordRepositoryEntity.setId(dto.getId());
        passwordRepositoryEntity.setName(dto.getName());
        passwordRepositoryEntity.setUrlImage(dto.getUrlImage());

        Optional.ofNullable(dto.getFirstPassword())
                .map(PasswordHistoryDto::getId)
                .ifPresent(passwordRepositoryEntity::setFirstPassId);

        Optional.ofNullable(dto.getLastPassword())
                .map(PasswordHistoryDto::getId)
                .ifPresent(passwordRepositoryEntity::setLastPassId);

        Optional.ofNullable(dto.getPrevPassword())
                .map(PasswordHistoryDto::getId)
                .ifPresent(passwordRepositoryEntity::setPrevPassId);

        List<PasswordHistoryEntity> histories = Optional.ofNullable(dto.getHistoryPasswords())
                .orElse(Collections.emptyList())
                .stream()
                .map(passwordHistoryConverter::convertToModel)
                .collect(Collectors.toList());
        passwordRepositoryEntity.setPasswords(histories);

        return passwordRepositoryEntity;
    }

    @Override
    public PasswordRepositoryDto convertToDto(PasswordRepositoryEntity model) {
        return null;
    }

    @Override
    public PasswordRepositoryDto updateData(PasswordRepositoryDto oldDto, PasswordRepositoryEntity newModel) {
        return null;
    }
}

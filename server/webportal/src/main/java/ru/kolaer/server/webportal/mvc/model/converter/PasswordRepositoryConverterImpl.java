package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
@Component
public class PasswordRepositoryConverterImpl implements PasswordRepositoryConverter {

    @Override
    public PasswordRepositoryEntity convertToModel(PasswordRepositoryDto dto) {
        PasswordRepositoryEntity passwordRepositoryEntity = new PasswordRepositoryEntity();
        passwordRepositoryEntity.setId(dto.getId());
        passwordRepositoryEntity.setName(dto.getName());
        passwordRepositoryEntity.setAccountId(dto.getAccountId());
        passwordRepositoryEntity.setUrlImage(dto.getUrlImage());

        return passwordRepositoryEntity;
    }

    @Override
    public PasswordRepositoryDto convertToDto(PasswordRepositoryEntity model) {
        return updateData(new PasswordRepositoryDto(), model);
    }

    @Override
    public PasswordRepositoryDto updateData(PasswordRepositoryDto oldDto, PasswordRepositoryEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());
        oldDto.setUrlImage(newModel.getUrlImage());
        oldDto.setAccountId(newModel.getAccountId());
        return oldDto;
    }
}

package ru.kolaer.server.kolpass.service;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.kolpass.model.entity.PasswordHistoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class PasswordHistoryConverterImpl implements PasswordHistoryConverter {

    @Override
    public PasswordHistoryEntity convertToModel(PasswordHistoryDto dto) {
        PasswordHistoryEntity passwordHistoryEntity = new PasswordHistoryEntity();
        passwordHistoryEntity.setId(dto.getId());
        passwordHistoryEntity.setLogin(dto.getLogin());
        passwordHistoryEntity.setPassword(dto.getPassword());
        passwordHistoryEntity.setPasswordWriteDate(dto.getPasswordWriteDate());
        passwordHistoryEntity.setDeadline(dto.getDeadline());
        return passwordHistoryEntity;
    }

    @Override
    public PasswordHistoryDto convertToDto(PasswordHistoryEntity model) {
        return updateData(new PasswordHistoryDto(), model);
    }

    @Override
    public PasswordHistoryDto updateData(PasswordHistoryDto oldDto, PasswordHistoryEntity newModel) {
        if(newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setLogin(newModel.getLogin());
        oldDto.setPassword(newModel.getPassword());
        oldDto.setPasswordWriteDate(newModel.getPasswordWriteDate());
        oldDto.setPasswordWriteDate(newModel.getDeadline());
        return oldDto;
    }
}

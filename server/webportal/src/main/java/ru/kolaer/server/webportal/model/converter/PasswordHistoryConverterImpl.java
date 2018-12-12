package ru.kolaer.server.webportal.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.webportal.model.entity.kolpass.PasswordHistoryEntity;

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

package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
@Service
public class PasswordHistoryConverterImpl implements PasswordHistoryConverter {
    @Override
    public PasswordHistoryEntity convertToModel(PasswordHistoryDto dto) {
        return null;
    }

    @Override
    public PasswordHistoryDto convertToDto(PasswordHistoryEntity model) {
        return null;
    }

    @Override
    public PasswordHistoryDto updateData(PasswordHistoryDto oldDto, PasswordHistoryEntity newModel) {
        return null;
    }
}

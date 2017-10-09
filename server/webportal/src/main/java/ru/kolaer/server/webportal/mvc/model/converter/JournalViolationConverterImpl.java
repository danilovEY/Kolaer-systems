package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolationDto;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class JournalViolationConverterImpl implements JournalViolationConverter {

    @Override
    public JournalViolationEntity convertToModel(JournalViolationDto dto) {
        JournalViolationEntity journalViolationEntity = new JournalViolationEntity();
        return journalViolationEntity;
    }

    @Override
    public JournalViolationDto convertToDto(JournalViolationEntity model) {
        return null;
    }

    @Override
    public JournalViolationDto updateData(JournalViolationDto oldDto, JournalViolationEntity newModel) {
        return null;
    }
}

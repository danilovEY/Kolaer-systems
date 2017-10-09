package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolationDto;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface JournalViolationService extends DefaultService<JournalViolationDto> {
    Page<JournalViolationDto> getAllJournal(Integer number, Integer pageSize);
    Page<JournalViolationDto> getAllByDep(Integer id);
    Page<JournalViolationDto> getByPnumberWriter(Integer id);
    Page<JournalViolationDto> getAllByDep(Integer id, Integer number, Integer pageSize);
    Page<JournalViolationDto> getByPnumberWriter(Integer id, Integer number, Integer pageSize);

    Long getCountByPnumberWriter(Integer id);
}

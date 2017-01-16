package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface JournalViolationService extends ServiceBase<JournalViolation> {
    Page<JournalViolation> getAllJournal(Integer number, Integer pageSize);
    Page<JournalViolation> getAllByDep(Integer id);
    Page<JournalViolation> getByPnumberWriter(Integer id);
    Page<JournalViolation> getAllByDep(Integer id, Integer number, Integer pageSize);
    Page<JournalViolation> getByPnumberWriter(Integer id, Integer number, Integer pageSize);

    Long getCountByPnumberWriter(Integer id);
}

package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface ViolationService extends ServiceBase<Violation> {
    void deleteByJournalId(Integer idJournal);
    List<Violation> getAllByJournalAndEffectiveOrWriter(Integer idJournal, Integer pnumber);
    Page<Violation> getByIdJournal(Integer id);
    Page<Violation> getByIdJournal(Integer id, Integer number, Integer pageSize);
}

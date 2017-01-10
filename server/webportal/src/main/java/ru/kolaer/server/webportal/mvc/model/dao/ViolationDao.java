package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface ViolationDao extends DefaultDao<Violation> {
    List<Violation> findByJournalAndEffectiveOrPnumber(Integer idJournal, Integer pnumber);
    List<Violation> findByJournalId(Integer id);

    void deleteByJournalId(Integer idJournal);
}

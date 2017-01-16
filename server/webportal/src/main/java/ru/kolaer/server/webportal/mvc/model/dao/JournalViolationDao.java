package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface JournalViolationDao extends DefaultDao<JournalViolation> {
    Page<JournalViolation> findAllJournal(Integer number, Integer pageSize);

    Page<JournalViolation> findAllByDep(Integer id, Integer number, Integer pageSize);
    Page<JournalViolation> findByPnumberWriter(Integer id, Integer number, Integer pageSize);
    Long getCountByPnumberWriter(Integer id);
}

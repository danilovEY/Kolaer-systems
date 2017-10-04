package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationEntity;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface JournalViolationDao extends DefaultDao<JournalViolationEntity> {
    Page<JournalViolationEntity> findAllJournal(Integer number, Integer pageSize);

    Page<JournalViolationEntity> findAllByDep(Integer id, Integer number, Integer pageSize);
    Page<JournalViolationEntity> findByPnumberWriter(Integer id, Integer number, Integer pageSize);
    Long getCountByPnumberWriter(Integer id);
}

package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface ViolationDao extends DefaultDao<Violation> {
    List<Violation> findByJournalAndPnumber(Integer idJournal, Integer pnumber);
    List<Violation> findByJournalAndEffective(Integer idJournal);
    List<Violation> findByJournalId(Integer id);
    Page<Violation> findByJournalId(Integer id, Integer number, Integer pageSize);

    void deleteByJournalId(Integer idJournal);

    List<Violation> findAllEffective();

    List<Violation> findAllEffectiveBenween(Date createStart, Date createEnd);
    List<Violation> findByJournalAndEffectiveBetween(Integer idJournal, Date createStart, Date createEnd);

    Long findCountViolationEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd);
}



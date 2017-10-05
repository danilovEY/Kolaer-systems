package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface ViolationDao extends DefaultDao<ViolationEntity> {
    List<ViolationEntity> findByJournalAndPnumber(Integer idJournal, Integer pnumber);
    List<ViolationEntity> findByJournalAndEffective(Integer idJournal);
    List<ViolationEntity> findByJournalId(Integer id);
    Page<ViolationEntity> findByJournalId(Integer id, Integer number, Integer pageSize);

    void deleteByJournalId(Integer idJournal);

    List<ViolationEntity> findAllEffective();

    List<ViolationEntity> findAllEffectiveBenween(Date createStart, Date createEnd);
    List<ViolationEntity> findByJournalAndEffectiveBetween(Integer idJournal, Date createStart, Date createEnd);

    Long findCountViolationEntityEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd);
}



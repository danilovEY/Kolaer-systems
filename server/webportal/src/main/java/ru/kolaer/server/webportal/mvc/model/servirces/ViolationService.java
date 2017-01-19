package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface ViolationService extends ServiceBase<Violation> {
    void deleteByJournalId(Integer idJournal);
    List<Violation> getAllByJournalAndWriter(Integer idJournal, Integer pnumber);
    List<Violation> getAllByJournalAndEffective(Integer idJournal, Date createStart, Date createEnd);
    List<Violation> getAllEffective(Date createStart, Date createEnd);
    Page<Violation> getByIdJournal(Integer id);
    Page<Violation> getByIdJournal(Integer id, Integer number, Integer pageSize);
    Long getCountViolationEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd);
}

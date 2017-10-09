package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.ViolationDto;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
public interface ViolationService extends DefaultService<ViolationDto> {
    void deleteByJournalId(Integer idJournal);
    List<ViolationDto> getAllByJournalAndWriter(Integer idJournal, Integer pnumber);
    List<ViolationDto> getAllByJournalAndEffective(Integer idJournal, Date createStart, Date createEnd);
    List<ViolationDto> getAllEffective(Date createStart, Date createEnd);
    Page<ViolationDto> getByIdJournal(Integer id);
    Page<ViolationDto> getByIdJournal(Integer id, Integer number, Integer pageSize);
    Long getCountViolationEffectiveByTypeBetween(Integer idType, StageEnum stage, Date createStart, Date createEnd);
}

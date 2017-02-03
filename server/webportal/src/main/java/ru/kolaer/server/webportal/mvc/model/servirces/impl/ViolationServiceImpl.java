package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.StageEnum;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.ViolationService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 21.09.2016.
 */
@Service
public class ViolationServiceImpl implements ViolationService {

    @Autowired
    private ViolationDao violationDao;

    @Override
    public List<Violation> getAll() {
        return this.violationDao.findAll();
    }

    @Override
    public Violation getById(Integer id) {
        return this.violationDao.findByID(id);
    }

    @Override
    public void add(Violation entity) {
        this.violationDao.persist(entity);
    }

    @Override
    public void delete(Violation entity) {
        this.violationDao.delete(entity);
    }

    @Override
    public void update(Violation entity) {
        this.violationDao.update(entity);
    }

    @Override
    public void update(List<Violation> entity) {

    }

    @Override
    public void delete(List<Violation> violations) {
        this.violationDao.delete(violations);
    }

    @Override
    public void deleteByJournalId(Integer idJournal) {
        this.violationDao.deleteByJournalId(idJournal);
    }

    @Override
    public List<Violation> getAllByJournalAndEffective(Integer idJournal, Date createStart, Date createEnd) {
        return createStart == null && createEnd == null ? this.violationDao.findByJournalAndEffective(idJournal)
                : this.violationDao.findByJournalAndEffectiveBetween(idJournal, createStart, createEnd);
    }

    @Override
    public List<Violation> getAllEffective(Date createStart, Date createEnd) {
        return createEnd == null && createStart == null ? this.violationDao.findAllEffective()
                : this.violationDao.findAllEffectiveBenween(createStart, createEnd);
    }

    @Override
    public List<Violation> getAllByJournalAndWriter(Integer idJournal, Integer pnumber) {
        return this.violationDao.findByJournalAndPnumber(idJournal, pnumber);
    }

    @Override
    public Page<Violation> getByIdJournal(Integer id) {
        return this.getByIdJournal(id, 0, 0);
    }

    @Override
    public Page<Violation> getByIdJournal(Integer id, Integer number, Integer pageSize) {
        return this.violationDao.findByJournalId(id,
                Optional.ofNullable(number).orElse(0),
                Optional.ofNullable(pageSize).orElse(0));
    }

    @Override
    public Long getCountViolationEffectiveByTypeBetween(Integer idType, StageEnum stageEnum, Date createStart, Date createEnd) {
        if(idType < 0 && createStart == null && createEnd == null)
            return 0L;
        return this.violationDao.findCountViolationEffectiveByTypeBetween(idType, stageEnum, createStart, createEnd);
    }
}

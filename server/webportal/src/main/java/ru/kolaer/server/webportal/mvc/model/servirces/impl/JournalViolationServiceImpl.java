package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.server.webportal.mvc.model.dao.JournalViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.JournalViolationService;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 21.09.2016.
 */
@Service
public class JournalViolationServiceImpl implements JournalViolationService {

    @Autowired
    private JournalViolationDao journalViolationDao;

    @Override
    public List<JournalViolation> getAll() {
        return this.journalViolationDao.findAll();
    }

    @Override
    public JournalViolation getById(Integer id) {
        return this.journalViolationDao.findByPersonnelNumber(id);
    }

    @Override
    public void add(JournalViolation entity) {
        this.journalViolationDao.persist(entity);
    }

    @Override
    public void delete(JournalViolation entity) {
        this.journalViolationDao.delete(entity);
    }

    @Override
    public void update(JournalViolation entity) {
        this.journalViolationDao.update(entity);
    }

    @Override
    public void update(List<JournalViolation> entity) {

    }

    @Override
    public void delete(List<JournalViolation> entites) {

    }

    @Override
    public Page<JournalViolation> getAllJournal(Integer number, Integer pageSize) {
        return this.journalViolationDao.findAllJournal(Optional.ofNullable(number).orElse(0),
                Optional.ofNullable(pageSize).orElse(0));
    }

    @Override
    public Page<JournalViolation> getAllByDep(Integer id) {
        return this.journalViolationDao.findAllByDep(id, 0, 0);
    }

    @Override
    public Page<JournalViolation> getByPnumberWriter(Integer id) {
       return this.getByPnumberWriter(id, 0, 0);
    }

    @Override
    public Page<JournalViolation> getAllByDep(Integer id, Integer number, Integer pageSize) {
        return this.journalViolationDao.findAllByDep(id,
                Optional.ofNullable(number).orElse(0),
                Optional.ofNullable(pageSize).orElse(0));
    }

    @Override
    public Page<JournalViolation> getByPnumberWriter(Integer id, Integer number, Integer pageSize) {
        return this.journalViolationDao.findByPnumberWriter(id,
                Optional.ofNullable(number).orElse(0),
                Optional.ofNullable(pageSize).orElse(0));
    }

    @Override
    public Long getCountByPnumberWriter(Integer id) {
        return this.journalViolationDao.getCountByPnumberWriter(id);
    }
}

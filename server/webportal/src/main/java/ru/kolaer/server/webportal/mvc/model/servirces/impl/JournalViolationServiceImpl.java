package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.server.webportal.mvc.model.dao.JournalViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.JournalViolationService;

import java.util.List;

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
        return this.journalViolationDao.findByID(id);
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
    public List<JournalViolation> getAllByDep(String depName) {
        return this.journalViolationDao.findAllByDep(depName);
    }
}

package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.ViolationService;

import java.util.List;

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

    }

    @Override
    public List<Violation> getAllByJournalAndEffective(Integer idJournal) {
        return this.violationDao.findByJournalAndEffective(idJournal);
    }
}

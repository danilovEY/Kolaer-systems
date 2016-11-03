package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.servirces.CounterService;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service("counterService")
public class CounterServiceImpl implements CounterService {

    @Autowired
    private CounterDao counterDao;

    @Override
    public List<Counter> getAll() {
        return counterDao.findAll();
    }

    @Override
    public Counter getById(Integer id) {
        if(id >= 0)
            return this.counterDao.findByID(id);
        return null;
    }

    @Override
    public void add(Counter entity) {
        if(entity != null)
            this.counterDao.persist(entity);
    }

    @Override
    public void delete(Counter entity) {
        if(entity != null)
            this.counterDao.persist(entity);
    }

    @Override
    public void update(Counter entity) {
        if(entity != null)
            this.counterDao.update(entity);
    }

    @Override
    public void update(List<Counter> entity) {

    }
}

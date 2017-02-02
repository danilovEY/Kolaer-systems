package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.CounterDecorator;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
@Repository("counterDao")
public class CounterDaoImpl implements CounterDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Counter> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(CounterDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public Counter findByPersonnelNumber(Integer id) {
        return this.sessionFactory.getCurrentSession().get(CounterDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(Counter obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(Counter obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(List<Counter> objs) {

    }

    @Override
    @Transactional
    public void update(Counter entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(List<Counter> objs) {

    }
}

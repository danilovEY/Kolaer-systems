package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
@Repository
public class ViolationDaoImpl implements ViolationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Violation> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(ViolationDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public Violation findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(ViolationDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(Violation obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(Violation obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(@NotNull(message = "Объект NULL!") List<Violation> objs) {

    }

    @Override
    @Transactional
    public void update(Violation entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(@NotNull(message = "Объект NULL!") List<Violation> objs) {

    }
}

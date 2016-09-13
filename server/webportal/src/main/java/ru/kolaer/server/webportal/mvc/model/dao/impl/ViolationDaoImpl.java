package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.DaoStandard;
import ru.kolaer.server.webportal.mvc.model.dao.ViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;

import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
@Repository
public class ViolationDaoImpl implements ViolationDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Violation> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(ViolationDecorator.class).list();
    }

    @Override
    public Violation findByID(int id) {
        return null;
    }

    @Override
    public void persist(Violation obj) {

    }

    @Override
    public void delete(Violation obj) {

    }

    @Override
    public void update(Violation entity) {

    }
}

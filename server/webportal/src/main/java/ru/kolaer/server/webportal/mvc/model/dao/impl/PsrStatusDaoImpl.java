package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.mvc.model.dao.PsrStatusDao;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import java.util.List;

/**
 * Created by Danilov on 07.08.2016.
 */
@Repository("psrSrarusDao")
public class PsrStatusDaoImpl implements PsrStatusDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<PsrStatus> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(PsrStatusDecorator.class).list();
    }

    @Override
    public PsrStatus findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(PsrStatusDecorator.class, id);
    }

    @Override
    public void persist(PsrStatus obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.mvc.model.dao.PsrStatusDao;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Danilov on 07.08.2016.
 */
@Repository("psrStatusDao")
public class PsrStatusDaoImpl implements PsrStatusDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<PsrStatus> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(PsrStatusDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public PsrStatus findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(PsrStatusDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(PsrStatus obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(PsrStatus obj) {

    }

    @Override
    public void delete(List<PsrStatus> objs) {

    }

    @Override
    @Transactional
    public void update(PsrStatus entity) {

    }

    @Override
    public void update(List<PsrStatus> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public PsrStatus getStatusByType(String type) {
        return (PsrStatus) this.sessionFactory.getCurrentSession()
                .createQuery("FROM PsrStatusDecorator st WHERE st.type = :status")
                .setParameter("status", type)
                .uniqueResult();
    }
}

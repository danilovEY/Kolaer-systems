package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.Page;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@Repository("psrRegisterDao")
public class PsrRegisterDaoImpl implements PsrRegisterDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<PsrRegister> findAll() {
        List<PsrRegister> list = this.sessionFactory.getCurrentSession().createQuery("FROM PsrRegisterDecorator reg").list();
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public PsrRegister findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(PsrRegisterDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(PsrRegister obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(PsrRegister obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void update(PsrRegister entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PsrRegister> getIdAndNamePsrRegister() {
        return this.sessionFactory.getCurrentSession().createCriteria(PsrRegisterDecorator.class)
                .setProjection(Projections.projectionList()
                        .add(Projections.property("id"), "id")
                        .add(Projections.property("name"), "name"))
                .setResultTransformer(Transformers.aliasToBean(PsrRegisterDecorator.class)).list();
    }

    @Override
    @Transactional
    public void deleteById(Integer ID) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM PsrRegisterDecorator reg WHERE reg.id = :id")
                .setParameter("id", ID).executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public PsrRegister getPsrRegisterByName(String name) {
        return (PsrRegister) this.sessionFactory.getCurrentSession()
                .createQuery("FROM PsrRegisterDecorator psr WHERE psr.name = :name")
                .setParameter("name", name).uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountEqualsPsrRegister(PsrRegister psrRegister) {
        return (Long) this.sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM PsrRegisterDecorator psr " +
                "WHERE psr.name = :name AND psr.comment = :comment")
                .setParameter("name", psrRegister.getName())
                .setParameter("comment", psrRegister.getComment())
                .uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PsrRegister> getPsrRegisterByStatusTitleComment(Integer status, String name, String comment) {
        return this.sessionFactory.getCurrentSession().createQuery("FROM PsrRegisterDecorator psr " +
                "WHERE psr.status.id = :status AND psr.name = :name AND psr.comment = :comment")
                .setParameter("status", status)
                .setParameter("name", name)
                .setParameter("comment", comment)
                .list();
    }

    @Transactional(readOnly = true)
    public Page<PsrRegister> findAll(Integer number, Integer pageSize) {
        Session currentSession = this.sessionFactory.getCurrentSession();
        final Long count = (Long) currentSession.createQuery("SELECT COUNT(reg.id) FROM PsrRegisterDecorator reg")
                .uniqueResult();
        List<PsrRegister> list = currentSession.createQuery("FROM PsrRegisterDecorator reg")
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();

        return new Page<>(list, number, count, pageSize);

    }
}

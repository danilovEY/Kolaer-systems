package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@Repository("psrRegisterDao")
public class PsrRegisterDaoImpl extends AbstractDefaultDao<PsrRegisterEntity> implements PsrRegisterDao {

    protected PsrRegisterDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PsrRegisterEntity.class);
    }

    @Override
    public List<PsrRegisterEntity> getIdAndNamePsrRegister() {
        return getSession()
                .createQuery("SELECT id, name FROM " + getEntityName(), getEntityClass())
                .list();
    }

    @Override
    public PsrRegisterEntity getPsrRegisterByName(String name) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " psr WHERE psr.name = :name", getEntityClass())
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public Long getCountEqualsPsrRegister(PsrRegisterEntity psrRegister) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM " + getEntityName() +
                        "WHERE name = :name AND comment = :comment", Long.class)
                .setParameter("name", psrRegister.getName())
                .setParameter("comment", psrRegister.getComment())
                .uniqueResult();
    }


    @Override
    public List<PsrRegisterEntity> getPsrRegisterByStatusTitleComment(Integer status, String name, String comment) {
        return getSession().createQuery("FROM " + getEntityName() +
                "WHERE status.id = :status AND name = :name AND comment = :comment", getEntityClass())
                .setParameter("status", status)
                .setParameter("name", name)
                .setParameter("comment", comment)
                .list();
    }
}

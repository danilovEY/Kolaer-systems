package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
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
    public List<PsrRegister> findAll() {
        List<PsrRegister> list = this.sessionFactory.getCurrentSession().createQuery("FROM PsrRegisterDecorator reg JOIN FETCH reg.author").list();
        list.parallelStream().forEach(psr -> {
            psr.getAttachments().size();
            psr.getStateList().size();
        });
        return list;
    }

    @Override
    public PsrRegister findByID(int id) {
        return null;
    }

    @Override
    public void persist(PsrRegister obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public List<PsrRegister> getIdAndNamePsrRegister() {
        return this.sessionFactory.getCurrentSession().createCriteria(PsrRegisterDecorator.class)
                .setProjection(Projections.projectionList()
                        .add(Projections.property("id"), "id")
                        .add(Projections.property("name"), "name"))
                .setResultTransformer(Transformers.aliasToBean(PsrRegisterDecorator.class)).list();
    }
}

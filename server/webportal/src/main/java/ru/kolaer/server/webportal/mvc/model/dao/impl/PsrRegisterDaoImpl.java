package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;

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
        List<PsrRegister> list = this.sessionFactory.getCurrentSession().createQuery("from PsrRegisterDecorator").list();
        return list;
    }

    @Override
    public PsrRegister findByID(int id) {
        return null;
    }

    @Override
    public void save(PsrRegister obj) {

    }
}

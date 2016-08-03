package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntityDecorator;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository(value = "userDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<GeneralAccountsEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralAccountsEntityDecorator.class).list();
    }

    @Override
    public GeneralAccountsEntityDecorator findByID(int id) {
        return null;
    }

    @Override
    @Transactional
    public void persist(GeneralAccountsEntity obj) {
        if(obj != null) {
            this.sessionFactory.getCurrentSession().persist(obj);
        }
    }

    @Override
    public GeneralAccountsEntity findName(String username) {
        return (GeneralAccountsEntity) this.sessionFactory.getCurrentSession()
                .createQuery("from GeneralAccountsEntityDecorator ac where ac.username=:username")
                .setParameter("username", username).uniqueResult();
    }
}

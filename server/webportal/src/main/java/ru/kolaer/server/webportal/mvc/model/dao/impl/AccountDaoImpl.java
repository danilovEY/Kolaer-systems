package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Repository(value = "accountDao")
public class AccountDaoImpl implements AccountDao {
    private static final Logger LOG = LoggerFactory.getLogger(AccountDaoImpl.class);
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
    public void delete(GeneralAccountsEntity obj) {

    }

    @Override
    public void update(GeneralAccountsEntity entity) {

    }

    @Override
    public GeneralAccountsEntity findName(String username) {
        return (GeneralAccountsEntity) this.sessionFactory.getCurrentSession()
                .createQuery("from GeneralAccountsEntityDecorator ac where ac.username=:username ")
                .setParameter("username", username).uniqueResult();
    }

    @Override
    public GeneralAccountsEntity getAccountByNameWithEmployee(String username) {
        final GeneralAccountsEntity acc = this.findName(username);
        Hibernate.initialize(acc.getGeneralEmployeesEntity());
        return acc;
    }
}

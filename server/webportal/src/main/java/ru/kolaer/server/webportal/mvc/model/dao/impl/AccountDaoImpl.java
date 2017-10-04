package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
@Slf4j
public class AccountDaoImpl implements AccountDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public AccountDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(AccountEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity findByID(Integer id) {
        return null;
    }

    @Override
    @Transactional
    public void persist(AccountEntity obj) {
        if(obj != null) {
            this.sessionFactory.getCurrentSession().persist(obj);
        }
    }

    @Override
    public void delete(AccountEntity obj) {

    }

    @Override
    public void delete(List<AccountEntity> objs) {

    }

    @Override
    public void update(AccountEntity entity) {

    }

    @Override
    public void update(List<AccountEntity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity findName(String username) {
        return (AccountEntity) this.sessionFactory.getCurrentSession()
                .createQuery("from GeneralAccountsEntityDecorator ac where ac.username=:username ")
                .setParameter("username", username).uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity getAccountByNameWithEmployee(String username) {
        final AccountEntity acc = this.findName(username);
        if(acc != null)
            Hibernate.initialize(acc.getEmployeeEntity());
        return acc;
    }
}

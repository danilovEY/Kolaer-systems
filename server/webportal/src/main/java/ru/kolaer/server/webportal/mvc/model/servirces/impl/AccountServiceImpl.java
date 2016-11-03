package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
//@Service
@Deprecated
public class AccountServiceImpl implements AccountService{
    private static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Override
    public List<GeneralAccountsEntity> getAll() {
        return this.accountDao.findAll();
    }

    @Override
    public GeneralAccountsEntity getByLogin(String login) {
        if(login != null &&!login.isEmpty())
            return this.accountDao.findName(login);

        LOG.error("Login is NULL!");
        return null;
    }

    @Override
    public GeneralAccountsEntity getById(Integer id) {
        if(id != null && id >= 0)
            return this.accountDao.findByID(id);

        LOG.error("Id is NULL or < 0!");
        return null;
    }

    @Override
    public void add(GeneralAccountsEntity accountsEntity) {
        if(accountsEntity == null) {
            LOG.error("Account is NULL!");
            return;
        } else if(accountsEntity.getGeneralEmployeesEntity() != null && accountsEntity.getGeneralEmployeesEntity().getPnumber() == null) {
            accountsEntity.setGeneralEmployeesEntity(null);
        }

        this.accountDao.persist(accountsEntity);

    }

    @Override
    public void delete(GeneralAccountsEntity entity) {
        if(entity != null) {
            this.accountDao.delete(entity);
        }
    }

    @Override
    public void update(GeneralAccountsEntity entity) {
        if(entity != null) {
            this.accountDao.persist(entity);
        }
    }

    @Override
    public void update(List<GeneralAccountsEntity> entity) {

    }
}

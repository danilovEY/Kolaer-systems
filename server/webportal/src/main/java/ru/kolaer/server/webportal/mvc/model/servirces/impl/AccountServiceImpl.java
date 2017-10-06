package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;

    @Override
    public List<AccountDto> getAll() {
        return this.accountDao.findAll();
    }

    @Override
    public AccountDto getByLogin(String login) {
        if(login != null &&!login.isEmpty())
            return this.accountDao.findName(login);

        LOG.error("Login is NULL!");
        return null;
    }

    @Override
    public AccountDto getById(Integer id) {
        if(id != null && id >= 0)
            return this.accountDao.findByID(id);

        LOG.error("Id is NULL or < 0!");
        return null;
    }

    @Override
    public void add(AccountDto accountsEntity) {
        if(accountsEntity == null) {
            LOG.error("Account is NULL!");
            return;
        } else if(accountsEntity.getEmployeeEntity() != null && accountsEntity.getEmployeeEntity().getPersonnelNumber() == null) {
            accountsEntity.setEmployeeEntity(null);
        }

        this.accountDao.persist(accountsEntity);

    }
}

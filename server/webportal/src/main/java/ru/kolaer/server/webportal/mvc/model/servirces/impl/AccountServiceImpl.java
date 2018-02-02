package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class AccountServiceImpl extends AbstractDefaultService<AccountDto, AccountEntity, AccountDao, AccountConverter> implements AccountService {

    @Autowired
    protected AccountServiceImpl(AccountDao defaultEntityDao,
                                 AccountConverter accountConverter) {
        super(defaultEntityDao, accountConverter);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getByLogin(String login) {
        if(login == null || login.isEmpty()){
            return null;
        }

        return defaultConverter.convertToDto(defaultEntityDao.findName(login));
    }

}

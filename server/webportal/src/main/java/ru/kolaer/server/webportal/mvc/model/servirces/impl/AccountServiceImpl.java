package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class AccountServiceImpl extends AbstractDefaultService<AccountDto, AccountEntity, AccountDao, AccountConverter> implements AccountService {

    private final AuthenticationService authenticationService;

    @Autowired
    protected AccountServiceImpl(AuthenticationService authenticationService,
                                 AccountDao defaultEntityDao,
                                 AccountConverter accountConverter) {
        super(defaultEntityDao, accountConverter);
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getByLogin(String login) {
        if(login == null || login.isEmpty()){
            return null;
        }

        return defaultConverter.convertToDto(defaultEntityDao.findName(login));
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto dto) {
        if(dto == null || dto.getId() == null) {
            throw new UnexpectedRequestParams("Не указан ID аккаунта");
        }

        AccountEntity currentAccount = defaultEntityDao.findName(authenticationService.getCurrentLogin());

        if(!currentAccount.isAccessOit() && !currentAccount.getId().equals(dto.getId())) {
            throw new ForbiddenException("У вас нет доступа для редактирования");
        }

        currentAccount.setUsername(dto.getUsername());
        currentAccount.setChatName(dto.getChatName());
        currentAccount.setEmail(dto.getEmail());

        AccountDto result = defaultConverter.convertToDto(defaultEntityDao.update(currentAccount));

        authenticationService.resetOnLogin(dto.getUsername());

        return result;
    }
}

package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.converter.BankAccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.dto.BankAccountDto;
import ru.kolaer.server.webportal.mvc.model.dto.BankAccountRequest;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.BankAccountService;

@Service
public class BankAccountServiceImpl
        extends AbstractDefaultService<BankAccountDto, BankAccountEntity, BankAccountDao, BankAccountConverter>
        implements BankAccountService {

    protected BankAccountServiceImpl(BankAccountDao defaultEntityDao, BankAccountConverter converter) {
        super(defaultEntityDao, converter);
    }

    @Override
    @Transactional
    public BankAccountDto add(BankAccountRequest bankAccountRequest) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setCheck(bankAccountRequest.getCheck());
        bankAccountEntity.setEmployeeId(bankAccountRequest.getEmployeeId());

        return defaultConverter.convertToDto(defaultEntityDao.persist(bankAccountEntity));
    }

    @Override
    @Transactional
    public BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest) {
        BankAccountEntity bankAccountEntity = defaultEntityDao.findById(bankId);
        bankAccountEntity.setEmployeeId(bankAccountRequest.getEmployeeId());
        bankAccountEntity.setCheck(bankAccountRequest.getCheck());

        return defaultConverter.convertToDto(defaultEntityDao.update(bankAccountEntity));
    }
}

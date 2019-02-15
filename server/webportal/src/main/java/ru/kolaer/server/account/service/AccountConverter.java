package ru.kolaer.server.account.service;

import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface AccountConverter extends BaseConverter<AccountDto, AccountEntity> {
    AccountEntity convertToModel(EmployeeDto employeeDto);
    AccountEntity convertToModel(EmployeeEntity employeeEntity);
    AccountSimpleDto convertToSimpleDto(AccountEntity accountEntity);

    AccountSimpleDto convertToSimpleDto(AccountDto dto);
}

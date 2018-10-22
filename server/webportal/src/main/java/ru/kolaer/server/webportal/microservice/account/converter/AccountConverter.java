package ru.kolaer.server.webportal.microservice.account.converter;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.microservice.account.AccountEntity;
import ru.kolaer.server.webportal.microservice.employee.EmployeeEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface AccountConverter extends BaseConverter<AccountDto, AccountEntity> {
    AccountEntity convertToModel(EmployeeDto employeeDto);
    AccountEntity convertToModel(EmployeeEntity employeeEntity);
    AccountSimpleDto convertToSimpleDto(AccountEntity accountEntity);

    AccountSimpleDto convertToSimpleDto(AccountDto dto);
}

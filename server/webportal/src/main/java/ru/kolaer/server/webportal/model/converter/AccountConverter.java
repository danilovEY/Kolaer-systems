package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.entity.general.AccountEntity;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface AccountConverter extends BaseConverter<AccountDto, AccountEntity> {
    AccountEntity convertToModel(EmployeeDto employeeDto);
    AccountEntity convertToModel(EmployeeEntity employeeEntity);
    AccountSimpleDto convertToSimpleDto(AccountEntity accountEntity);

    AccountSimpleDto convertToSimpleDto(AccountDto dto);
}

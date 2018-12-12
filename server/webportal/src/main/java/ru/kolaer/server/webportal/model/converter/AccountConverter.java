package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.model.entity.general.AccountEntity;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;
import ru.kolaer.server.webportal.model.servirce.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface AccountConverter extends BaseConverter<AccountDto, AccountEntity> {
    AccountEntity convertToModel(EmployeeDto employeeDto);
    AccountEntity convertToModel(EmployeeEntity employeeEntity);
    AccountSimpleDto convertToSimpleDto(AccountEntity accountEntity);

    AccountSimpleDto convertToSimpleDto(AccountDto dto);
}

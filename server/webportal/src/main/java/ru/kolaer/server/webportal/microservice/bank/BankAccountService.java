package ru.kolaer.server.webportal.microservice.bank;

import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.common.dto.FilterParam;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.bank.BankAccountDto;
import ru.kolaer.server.webportal.microservice.bank.BankAccountRequest;

public interface BankAccountService extends DefaultService<BankAccountDto> {
    BankAccountDto add(BankAccountRequest bankAccountRequest);

    BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest);

    Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize);
}

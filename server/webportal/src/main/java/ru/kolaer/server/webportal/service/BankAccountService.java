package ru.kolaer.server.webportal.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.bank.BankAccountDto;
import ru.kolaer.server.webportal.model.dto.bank.BankAccountRequest;

public interface BankAccountService extends DefaultService<BankAccountDto> {
    BankAccountDto add(BankAccountRequest bankAccountRequest);

    BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest);

    Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize);
}

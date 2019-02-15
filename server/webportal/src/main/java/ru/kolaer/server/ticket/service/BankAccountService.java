package ru.kolaer.server.ticket.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.model.dto.FilterParam;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.ticket.model.dto.BankAccountDto;
import ru.kolaer.server.ticket.model.request.BankAccountRequest;

public interface BankAccountService extends DefaultService<BankAccountDto> {
    BankAccountDto add(BankAccountRequest bankAccountRequest);

    BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest);

    Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize);
}

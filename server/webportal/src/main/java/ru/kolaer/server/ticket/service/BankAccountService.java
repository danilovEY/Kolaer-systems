package ru.kolaer.server.ticket.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.ticket.model.dto.BankAccountDto;
import ru.kolaer.server.ticket.model.request.BankAccountRequest;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.SortParam;

public interface BankAccountService extends DefaultService<BankAccountDto> {
    BankAccountDto add(BankAccountRequest bankAccountRequest);

    BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest);

    Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize);
}

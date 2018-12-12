package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.bank.BankAccountDto;
import ru.kolaer.server.webportal.model.dto.bank.BankAccountRequest;

public interface BankAccountService extends DefaultService<BankAccountDto> {
    BankAccountDto add(BankAccountRequest bankAccountRequest);

    BankAccountDto update(Long bankId, BankAccountRequest bankAccountRequest);

    Page<EmployeeDto> getAllEntityWithAccount(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize);
}

package ru.kolaer.server.ticket.collection;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.TicketAccessConstant;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.employee.model.request.EmployeeFilter;
import ru.kolaer.server.employee.model.request.EmployeeSort;
import ru.kolaer.server.ticket.model.dto.BankAccountDto;
import ru.kolaer.server.ticket.model.request.BankAccountFilter;
import ru.kolaer.server.ticket.model.request.BankAccountRequest;
import ru.kolaer.server.ticket.model.request.BankAccountSort;
import ru.kolaer.server.ticket.service.BankAccountService;

/**
 * Created by danilovey on 03.10.2017.
 */
@RestController
@Api(tags = "Банковские счета")
@RequestMapping("/bank")
@RequiredArgsConstructor
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @ApiOperation(value = "Получить все счета")
    @PreAuthorize("hasRole('" + TicketAccessConstant.BANK_ACCOUNTS_READ + "')")
    @GetMapping(RouterConstants.BANK)
    public Page<BankAccountDto> getAll(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                       BankAccountSort sortParam,
                                       BankAccountFilter filter) {
        return bankAccountService.getAll(sortParam, filter, pageNum, pageSize);
    }

    @ApiOperation(value = "Получить сотрудников которые имеют счета")
    @PreAuthorize("hasRole('" + TicketAccessConstant.BANK_ACCOUNTS_READ + "')")
    @GetMapping(RouterConstants.BANK_EMPLOYEE)
    public Page<EmployeeDto> getAllEmployees(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                             EmployeeSort sortParam,
                                             EmployeeFilter filter) {
        return bankAccountService.getAllEntityWithAccount(sortParam, filter, pageNum, pageSize);
    }

    @ApiOperation(value = "Добавить счет")
    @PreAuthorize("hasRole('" + TicketAccessConstant.BANK_ACCOUNTS_READ + "')")
    @PostMapping(RouterConstants.BANK)
    public BankAccountDto addBankAccount(@RequestBody BankAccountRequest bankAccountRequest) {
        return bankAccountService.add(bankAccountRequest);
    }

    @ApiOperation(value = "Обновит счет")
    @PreAuthorize("hasRole('" + TicketAccessConstant.BANK_ACCOUNTS_READ + "')")
    @PutMapping(RouterConstants.BANK_ID)
    public void deleteBankAccount(@PathVariable(PathVariableConstants.BANK_ACCOUNT_ID) Long bankAccountId,
                                  @RequestBody BankAccountRequest bankAccountRequest) {
        bankAccountService.update(bankAccountId, bankAccountRequest);
    }

    @ApiOperation(value = "Удалить счет")
    @PreAuthorize("hasRole('" + TicketAccessConstant.BANK_ACCOUNTS_READ + "')")
    @DeleteMapping(RouterConstants.BANK_ID)
    public void deleteBankAccount(@PathVariable(PathVariableConstants.BANK_ACCOUNT_ID) Long bankAccountId) {
        bankAccountService.delete(bankAccountId);
    }

}

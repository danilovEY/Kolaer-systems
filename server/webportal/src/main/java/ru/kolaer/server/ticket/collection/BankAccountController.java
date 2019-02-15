package ru.kolaer.server.ticket.collection;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.annotation.UrlDeclaration;
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
    @UrlDeclaration(description = "Получить все счета", isUser = false)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<BankAccountDto> getAll(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                       @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                       BankAccountSort sortParam,
                                       BankAccountFilter filter) {
        return bankAccountService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Получить сотрудников которые имеют счета")
    @UrlDeclaration(description = "Получить сотрудников которые имеют счета")
    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<EmployeeDto> getAllEmployees(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                             @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                             EmployeeSort sortParam,
                                             EmployeeFilter filter) {
        return bankAccountService.getAllEntityWithAccount(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Добавить счет")
    @UrlDeclaration(description = "Добавить счет", isUser = false, requestMethod = RequestMethod.POST)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BankAccountDto addBankAccount(@RequestBody BankAccountRequest bankAccountRequest) {
        return bankAccountService.add(bankAccountRequest);
    }

    @ApiOperation(value = "Обновит счет")
    @UrlDeclaration(description = "Обновит счет", isUser = false, requestMethod = RequestMethod.PUT)
    @RequestMapping(value = "/{bankId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteBankAccount(@PathVariable("bankId") Long bankId,
                                  @RequestBody BankAccountRequest bankAccountRequest) {
        bankAccountService.update(bankId, bankAccountRequest);
    }

    @ApiOperation(value = "Удалить счет")
    @UrlDeclaration(description = "Удалить счет", isUser = false, requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/{bankId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteBankAccount(@PathVariable("bankId") Long bankId) {
        bankAccountService.delete(bankId);
    }

}

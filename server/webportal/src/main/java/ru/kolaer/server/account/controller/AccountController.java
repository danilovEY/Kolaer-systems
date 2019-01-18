package ru.kolaer.server.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.server.account.AccountRoleConstant;
import ru.kolaer.server.account.service.AccountService;
import ru.kolaer.server.core.model.dto.account.AccountFilter;
import ru.kolaer.server.core.model.dto.account.AccountSort;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@Api(tags = "Аккаунты")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasAnyRole('" + AccountRoleConstant.ROLE_SUPER_ADMIN + "')")
    @ApiOperation(value = "Получить все аккаунты")
    @GetMapping(RouterConstants.ACCOUNTS)
    public Page<AccountDto> getAllAccounts(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                           @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                           AccountSort sortParam,
                                           AccountFilter filter) {
        return this.accountService.getAll(sortParam, filter, number, pageSize);
    }
}

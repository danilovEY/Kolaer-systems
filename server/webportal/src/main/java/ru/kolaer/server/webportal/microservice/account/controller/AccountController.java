package ru.kolaer.server.webportal.microservice.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.microservice.account.dto.AccountFilter;
import ru.kolaer.server.webportal.microservice.account.dto.AccountSort;
import ru.kolaer.server.webportal.microservice.account.service.AccountService;
import ru.kolaer.server.webportal.microservice.account.service.AuthenticationService;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/accounts")
@Api(tags = "Аккаунты")
@Slf4j
public class AccountController {
    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AuthenticationService authenticationService,
                             AccountService accountService) {
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    @ApiOperation(
            value = "Получить все аккаунты"
    )
    @UrlDeclaration(description = "Получить все аккаунты", isUser = true)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<AccountDto> getAllAccounts(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                           @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                           AccountSort sortParam,
                                           AccountFilter filter) {
        return this.accountService.getAll(sortParam, filter, number, pageSize);
    }
}

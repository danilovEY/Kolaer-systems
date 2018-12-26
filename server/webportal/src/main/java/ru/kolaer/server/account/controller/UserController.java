package ru.kolaer.server.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.account.service.AccountService;
import ru.kolaer.server.core.annotation.UrlDeclaration;
import ru.kolaer.server.core.model.dto.account.ChangePasswordDto;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.core.service.AuthenticationService;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "Мой аккаунт")
@Slf4j
public class UserController {
    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    @Autowired
    public UserController(AuthenticationService authenticationService,
                          AccountService accountService) {
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    @ApiOperation(
            value = "Получить авторизированный аккаунт"
    )
    @UrlDeclaration(description = "Получить авторизированный аккаунт с сотрудником")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto getUser() {
        return this.authenticationService.getAccountByAuthentication();
    }

    @ApiOperation(
            value = "Получить авторизированный аккаунт"
    )
    @UrlDeclaration(description = "Получить авторизированный аккаунт")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountSimpleDto getSimpleUser() {
        return this.authenticationService.getAccountSimpleByAuthentication();
    }

    /**Добавить аккаунт.*/
    @UrlDeclaration(description = "Обновить аккаунт.", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateUser(@RequestBody AccountSimpleDto accountEntity) {
        this.accountService.update(accountEntity);
    }

    /**Изменить пароль.*/
    @UrlDeclaration(description = "Изменить пароль.")
    @RequestMapping(value = "/update/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        accountService.updatePassword(changePasswordDto);
    }

    @ApiOperation(value = "Обновить контакты")
    @UrlDeclaration
    @RequestMapping(value = "/contact", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ContactDto updateContact(@RequestBody ContactRequestDto contactRequestDto) {
        return accountService.updateContact(contactRequestDto);
    }

    @ApiOperation(value = "Получить контакты")
    @UrlDeclaration
    @RequestMapping(value = "/contact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ContactDto getContact() {
        return accountService.getContact();
    }
}

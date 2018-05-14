package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "Аккаунт")
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
    @UrlDeclaration(description = "Получить авторизированный аккаунт с сотрудником", isUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto getUser() {
        return this.authenticationService.getAccountByAuthentication();
    }

    @ApiOperation(
            value = "Получить авторизированный аккаунт"
    )
    @UrlDeclaration(description = "Получить авторизированный аккаунт", isUser = true)
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

    /**Получить все аккаунты.*/
//    @UrlDeclaration(description = "Получить все аккаунты.")
//    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<AccountDto> getAllUsers() {
//        return accountService.getAll();
//    }

    /**Добавить аккаунт.*/
//    @UrlDeclaration(description = "Добавить аккаунт.", requestMethod = RequestMethod.POST)
//    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public void addUser(@RequestBody AccountDto accountEntity) {
//        this.accountService.save(accountEntity);
//    }
}

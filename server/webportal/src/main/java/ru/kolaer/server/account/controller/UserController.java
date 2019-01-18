package ru.kolaer.server.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.account.AccountRoleConstant;
import ru.kolaer.server.account.service.AccountService;
import ru.kolaer.server.core.model.dto.account.ChangePasswordDto;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.core.service.AuthenticationService;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
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

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation("Получить авторизированный аккаунт")
    @GetMapping(RouterConstants.USER_WITH_EMPLOYEE)
    public AccountDto getUser() {
        return this.authenticationService.getAccountByAuthentication();
    }

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation("Получить авторизированный аккаунт")
    @GetMapping(RouterConstants.USER)
    public AccountSimpleDto getSimpleUser() {
        return this.authenticationService.getAccountSimpleByAuthentication();
    }

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation("Обновить авторизированный аккаунт")
    @PutMapping(RouterConstants.USER)
    public void updateUser(@RequestBody AccountSimpleDto accountEntity) {
        this.accountService.update(accountEntity);
    }

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation("Изменить пароль")
    @PutMapping(RouterConstants.USER_PASSWORD)
    public void updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        accountService.updatePassword(changePasswordDto);
    }

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation(value = "Получить контакты")
    @GetMapping(RouterConstants.USER_CONTACT)
    public ContactDto getContact() {
        return accountService.getContact();
    }

    @PreAuthorize("hasRole('" + AccountRoleConstant.ROLE_USER + "')")
    @ApiOperation(value = "Обновить контакты")
    @PutMapping(RouterConstants.USER_CONTACT)
    public ContactDto updateContact(@RequestBody ContactRequestDto contactRequestDto) {
        return accountService.updateContact(contactRequestDto);
    }
}

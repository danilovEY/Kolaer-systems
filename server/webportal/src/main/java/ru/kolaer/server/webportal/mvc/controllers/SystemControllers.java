package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/system")
@Api(tags = "Системные функции")
public class SystemControllers {
    private final AuthenticationService authenticationService;

    @Autowired
    public SystemControllers(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(
            value = "Обновить кэш у авторизованного аккаунта"
    )
    @UrlDeclaration(description = "Обновить кэш у авторизованного аккаунта")
    @RequestMapping(value = "/cache/update", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto updateCacheByLogin() {
        return authenticationService.resetOnLogin(authenticationService.getAccountByAuthentication().getUsername());
    }

    @ApiOperation(
            value = "Обновить кэш у аккаунта"
    )
    @UrlDeclaration(description = "Обновить кэш у аккаунта", isUser = false)
    @RequestMapping(value = "/cache/update/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto updateCacheByLogin(@PathVariable String login) {
        return authenticationService.resetOnLogin(login);
    }

}

package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
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

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(
            value = "Получить авторизированный аккаунт"
    )
    @UrlDeclaration(description = "Получить авторизированный аккаунт", isUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            return null;
        return this.authenticationService.getAccountWithEmployeeByLogin(authentication.getName());
    }
}

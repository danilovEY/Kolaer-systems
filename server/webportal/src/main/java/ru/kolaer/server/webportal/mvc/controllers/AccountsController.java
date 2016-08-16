package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 * Рест контроллер для работы с аккаунтами.
 */
@RestController
@RequestMapping("/general/accounts")
public class AccountsController {
    private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);

    @Autowired
    private AccountService accountService;

    /**Получить все аккаунты.*/
    @UrlDeclaration(description = "Получить все аккаунты.")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GeneralAccountsEntity> getAllUsers() {
        return accountService.getAll();
    }

}

package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 27.07.2016.
 * Рест контроллер для работы с аккаунтами.
 */
@RestController
@RequestMapping("/accounts")
@Slf4j
public class AccountsController {

    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @UrlDeclaration(description = "Получить все аккаунты.", isAccessAll = true)
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> test() {
        throw new UsernameNotFoundException("!!");
        //return Collections.singletonMap("test", "true");
    }

    /**Получить все аккаунты.*/
    @UrlDeclaration(description = "Получить все аккаунты.")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccountDto> getAllUsers() {
        return accountService.getAll();
    }

    /**Добавить аккаунт.*/
    @UrlDeclaration(description = "Добавить аккаунт.", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addUser(AccountDto accountEntity) {
        this.accountService.save(accountEntity);
    }

}

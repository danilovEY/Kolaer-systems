package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.SeterProviderBean;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.impl.EmployeeLDAPImpl;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private SeterProviderBean seterProviderBean;

    @Autowired
    @Qualifier("accountDao")
    private AccountDao accountDao;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @UrlDeclaration(description = "Получить авторизированный аккаунт.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GeneralAccountsEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return null;

        return this.seterProviderBean.isLDAP() ?
                this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName())
                : this.accountDao.getAccountByNameWithEmployee(authentication.getName());
    }

    @UrlDeclaration(description = "Получить роли авторизированного аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/roles/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<EnumRole> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return Collections.emptyList();

        return null;
    }
}

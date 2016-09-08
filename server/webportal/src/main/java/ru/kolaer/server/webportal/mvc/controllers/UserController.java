package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.SeterProviderBean;
import ru.kolaer.server.webportal.beans.ToolsLDAP;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ServiceLDAP serviceLDAP;

    @UrlDeclaration(description = "Получить авторизированный аккаунт.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GeneralAccountsEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            return null;
        return this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName());
    }

    @UrlDeclaration(description = "Получить роли авторизированного аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/roles/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<EnumRole> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return Collections.emptyList();
        final GeneralAccountsEntity generalAccountsEntity = this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName());

        return generalAccountsEntity.getRoles().stream().map(GeneralRolesEntity::getType).collect(Collectors.toList());
    }

    @UrlDeclaration(description = "Получить фото аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/photo/get", method = RequestMethod.GET)
    public void showImage(HttpServletResponse response) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            throw new UsernameNotFoundException("Не авторизовались!");

        byte[] imgByte = this.serviceLDAP.getAccountPhoto(authentication.getName());

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        final ServletOutputStream responseOutputStream = response.getOutputStream();

        if(imgByte != null) {
            responseOutputStream.write(imgByte);
            responseOutputStream.flush();
        } else {
            GeneralAccountsEntity user = this.getUser();

            final String url = "http://asupkolaer/app_ie8/assets/images/vCard/o_" + URLEncoder.encode(user.getGeneralEmployeesEntity().getInitials(), "UTF-8").replace("+", "%20") + ".jpg";
            InputStream inputStream = URI.create(url).toURL().openStream();
            byte[] readByte = new byte[2048];
            int length;

            while ((length = inputStream.read(readByte)) != -1) {
                responseOutputStream.write(readByte, 0, length);
                responseOutputStream.flush();
            }
            inputStream.close();
        }

        responseOutputStream.close();
    }
}

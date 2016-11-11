package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.UserSessionInfo;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.Collections;
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

    @Autowired
    private UserSessionInfo userSessionInfo;

    @UrlDeclaration(description = "Получить авторизированный аккаунт.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GeneralAccountsEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            return null;
        return Optional.ofNullable(this.userSessionInfo.getGeneralAccountsEntity())
                .orElse(this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()));
    }

    @UrlDeclaration(description = "Получить роли авторизированного аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/roles/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<EnumRole> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return Collections.emptyList();

        final GeneralAccountsEntity generalAccountsEntity = Optional.ofNullable(this.userSessionInfo.getGeneralAccountsEntity())
                .orElse(this.serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()));

        return generalAccountsEntity.getRoles().stream().map(GeneralRolesEntity::getType).collect(Collectors.toList());
    }

    private byte[] getImageByte() throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            throw new UsernameNotFoundException("Не авторизовались!");

        byte[] imgByte = this.serviceLDAP.getAccountPhoto(authentication.getName());

        if(imgByte == null) {
            GeneralAccountsEntity user = this.getUser();

            final String url = user.getGeneralEmployeesEntity().getPhoto();
            InputStream inputStream = URI.create(url).toURL().openStream();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int r = inputStream.read(buffer);
                if (r == -1) break;
                out.write(buffer, 0, r);
            }

            imgByte = out.toByteArray();
            out.close();
            inputStream.close();
        }

        return imgByte;
    }

    @UrlDeclaration(description = "Получить фото аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/photo/get", method = RequestMethod.GET)
    public void showImage(HttpServletResponse response) throws Exception {
        final byte[] imgByte = this.getImageByte();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        final ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @UrlDeclaration(description = "Получить массив байт фото аккаунта.", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/photo/get/byte", method = RequestMethod.GET)
    public String getByteImage() throws Exception {
        final byte[] imgByte = this.getImageByte();

        byte[] encodeBase64 = Base64.encode(imgByte);
        return new String(encodeBase64, "UTF-8");
    }
}

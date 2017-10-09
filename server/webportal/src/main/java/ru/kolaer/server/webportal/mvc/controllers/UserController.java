package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.RoleDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "Аккаунт")
@Slf4j
public class UserController extends BaseController {
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(
            value = "Получить авторизированный аккаунт"
    )
    @UrlDeclaration(description = "Получить авторизированный аккаунт", isAccessUser = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            return null;
        return this.authenticationService.getAccountWithEmployeeByLogin(authentication.getName());
    }

    @ApiOperation(
            value = "Получить роли авторизированного аккаунта"
    )
    @UrlDeclaration(description = "Получить роли авторизированного аккаунта", isAccessUser = true)
    @RequestMapping(value = "/roles/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RoleDto> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return Collections.emptyList();

        final AccountDto accountEntity = this.authenticationService
                .getAccountWithEmployeeByLogin(authentication.getName());

        return accountEntity.getRoles();
    }

    private byte[] getImageByte() throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            throw new UsernameNotFoundException("Не авторизовались!");

        byte[] imgByte = this.authenticationService.getAccountPhoto(authentication.getName());

        if(imgByte == null) {
            AccountDto user = this.getUser();

            final String url = user.getEmployee().getPhoto();
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

    @ApiOperation(
            value = "Получить фото аккаунта"
    )
    @UrlDeclaration(description = "Получить фото аккаунта", isAccessUser = true)
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

    @ApiOperation(
            value = "Получить массив байт фото аккаунта"
    )
    @UrlDeclaration(description = "Получить массив байт фото аккаунта", isAccessUser = true)
    @RequestMapping(value = "/photo/get/byte", method = RequestMethod.GET)
    public String getByteImage() throws Exception {
        final byte[] imgByte = this.getImageByte();

        byte[] encodeBase64 = Base64.encode(imgByte);
        return new String(encodeBase64, "UTF-8");
    }
}

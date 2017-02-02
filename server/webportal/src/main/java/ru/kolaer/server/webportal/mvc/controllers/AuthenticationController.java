package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import ru.kolaer.server.webportal.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by danilovey on 28.07.2016.
 * Рест контроллер для генерации токена, и пароля по строке.
 */
@RestController
@RequestMapping(value = "/authentication")
@Api(description = "Работа с авторизацией", tags = "Аутентификация")
public class AuthenticationController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${secret_key}")
    private String secretKey;

    @Autowired
    private UserDetailsService userDetailsServiceLDAP;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Выйти"
    )
    @UrlDeclaration(description = "Выйти", requestMethod = RequestMethod.POST, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.clearContext();
        }
        return "redirect:/";
    }

    @ApiOperation(
            value = "Выйти"
    )
    @UrlDeclaration(description = "Выйти", requestMethod = RequestMethod.GET, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutGet(HttpServletResponse response, HttpServletRequest request) {
        return this.logout(response, request);
    }

    @ApiOperation(
            value = "Генерация пароля по строке",
            hidden = true
    )
    @UrlDeclaration(description = "Генерация пароля по строке")
    @RequestMapping(value = "/genpass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPass(@ApiParam(value = "Пароль", required = true) @RequestParam("pass") String pass) {
        return new StandardPasswordEncoder(secretKey).encode(pass);
    }

    @ApiOperation(
            value = "Авторизация",
            notes = "Генерация токена по имени и паролю пользователя."
    )
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя)", requestMethod = RequestMethod.POST, isAccessAll = true, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getTokenPost(@ApiParam(value = "Логин и пароль", required = true) @RequestBody UserAndPassJson userAndPassJson){
        return this.getToken(userAndPassJson.getUsername(), Optional.ofNullable(userAndPassJson.getPassword()).orElse(""));
    }


    @ApiOperation(
            value = "Авторизация",
            notes = "Генерация токена по имени и паролю пользователя"
    )
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя)", isAccessAll = true, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getToken(@ApiParam(value = "Логин", required = true) @RequestParam(value = "username", defaultValue = "anonymous") String username,
                              @ApiParam(value = "Пароль") @RequestParam(value = "password", defaultValue = "") String password){
        if(password == null)
            password = "";
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        final Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

        if(authentication == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден!");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = this.userDetailsServiceLDAP.loadUserByUsername(username);
        if(userDetails == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден!");
        }

        final String tokenJson = TokenUtils.createTokenLDAP(userDetails);
        return new TokenJson(tokenJson);
    }

}

package ru.kolaer.server.webportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.kolaerweb.TokenJson;
import ru.kolaer.common.dto.kolaerweb.UserAndPassJson;
import ru.kolaer.server.webportal.annotation.UrlDeclaration;
import ru.kolaer.server.webportal.service.impl.TokenService;

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
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    TokenService tokenService,
                                    PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @ApiOperation(
            value = "Выйти"
    )
    @UrlDeclaration(description = "Выйти", requestMethod = RequestMethod.POST, isAccessAll = true)
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
    @UrlDeclaration(description = "Выйти", requestMethod = RequestMethod.GET, isAccessAll = true)
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
        return passwordEncoder.encode(pass);
    }

    @ApiOperation(
            value = "Авторизация",
            notes = "Генерация токена по имени и паролю пользователя."
    )
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя)", requestMethod = RequestMethod.POST, isAccessAll = true)
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getTokenPost(@ApiParam(value = "Логин и пароль", required = true) @RequestBody UserAndPassJson userAndPassJson){
        return this.getToken(userAndPassJson.getUsername(), Optional.ofNullable(userAndPassJson.getPassword()).orElse(""));
    }

    @ApiOperation(
            value = "Авторизация",
            notes = "Генерация токена по имени и паролю пользователя"
    )
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя)", isAccessAll = true)
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getToken(@ApiParam(value = "Логин", required = true) @RequestParam(value = "username", defaultValue = "anonymous") String username,
                              @ApiParam(value = "Пароль") @RequestParam(value = "password", defaultValue = "") String password){
        if(password == null)
            password = "";

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

        if(authentication == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден!");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден!");
        }

        return new TokenJson(getToken(userDetails));
    }

    @ApiOperation(
            value = "Обновление токена",
            notes = "Обновление токена."
    )
    @UrlDeclaration(description = "Обновление токена", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson refreshToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new AccessDeniedException("Нет доступа");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        return new TokenJson(getToken(userDetails));
    }

    private String getToken(UserDetails userDetails) {
         return  tokenService.createToken(userDetails);
    }

}

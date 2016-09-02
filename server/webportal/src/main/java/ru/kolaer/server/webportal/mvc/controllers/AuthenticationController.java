package ru.kolaer.server.webportal.mvc.controllers;

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
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.SeterProviderBean;
import ru.kolaer.server.webportal.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 28.07.2016.
 * Рест контроллер для генерации токена, и пароля по строке.
 */
@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${secret_key}")
    private String secretKey;

    @Autowired
    private UserDetailsService userDetailsServiceLDAP;

    @Autowired
    private UserDetailsService userDetailsServiceSQL;

    @Autowired
    private SeterProviderBean seterProviderBean;


    @UrlDeclaration(description = "Выйти.", requestMethod = RequestMethod.POST, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    /**Генерация пароля по строке.*/
    @UrlDeclaration(description = "Генерация пароля по строке. (?pass={pass})", isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/genpass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPass(@RequestParam("pass") String pass) {
        return new StandardPasswordEncoder(secretKey).encode(pass);
    }

    /**Авторизация.*/
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя)", requestMethod = RequestMethod.POST, isAccessAll = true, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getTokenPost(@RequestBody UserAndPassJson userAndPassJson){
        return this.getToken(userAndPassJson.getUsername(), userAndPassJson.getPassword());
    }


    /**Генерация токена по имени и паролю пользователя.*/
    @UrlDeclaration(description = "Авторизация. (Генерация токена по имени и паролю пользователя) (?username={login}&password={pass})", isAccessAll = true, isAccessAnonymous = true, isAccessUser = true)
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getToken(@RequestParam(value = "username", defaultValue = "anonymous") String username,
                              @RequestParam(value = "password", defaultValue = "") String password){
        if(password == null)
            password = "";
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        final Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final boolean isLDAP = this.seterProviderBean.isLDAP();

        final UserDetails userDetails = isLDAP ? this.userDetailsServiceLDAP.loadUserByUsername(username) : this.userDetailsServiceSQL.loadUserByUsername(username);;

        final String tokenJson = isLDAP ? TokenUtils.createTokenLDAP(userDetails) : TokenUtils.createToken(userDetails);
        return new TokenJson(tokenJson);
    }

}

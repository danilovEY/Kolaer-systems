package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.server.webportal.security.TokenUtils;

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
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    /**Генерация пароля по строке.*/
    @RequestMapping(value = "/genpass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPass(@RequestParam("pass") String pass) {
        return new StandardPasswordEncoder(secretKey).encode(pass);
    }

    /**Генерация токена по имени и паролю пользователя.*/
    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        return new TokenJson(TokenUtils.createToken(userDetails));
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getTokenPost(@RequestBody UserAndPassJson userAndPassJson){
        return this.getToken(userAndPassJson.getUsername(), userAndPassJson.getPassword());
    }

}

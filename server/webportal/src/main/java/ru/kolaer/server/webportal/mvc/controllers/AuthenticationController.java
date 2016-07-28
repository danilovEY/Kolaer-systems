package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.server.webportal.mvc.model.TokenJson;
import ru.kolaer.server.webportal.mvc.model.UserAndPassJson;

/**
 * Created by danilovey on 28.07.2016.
 */
@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${secret_key}")
    private String secretKey;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/genpass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPass(@RequestParam("pass") String pass) {
        return new StandardPasswordEncoder(secretKey).encode(pass);
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        return new TokenJson(userDetails.getUsername() + ":" + userDetails.getPassword());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenJson getTokenPost(@RequestBody UserAndPassJson userAndPassJson){
        return this.getToken(userAndPassJson.getUsername(), userAndPassJson.getPassword());
    }

}

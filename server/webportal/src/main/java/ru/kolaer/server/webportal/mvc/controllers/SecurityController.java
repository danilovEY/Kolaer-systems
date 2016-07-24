package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.TokenJson;

import java.util.UUID;

/**
 * Created by Danilov on 24.07.2016.
 */
@RestController
@RequestMapping("/security")
public class SecurityController {

    @RequestMapping(value = "/authenticate",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenJson getToken() {
        return new TokenJson(UUID.randomUUID().toString());
    }

}

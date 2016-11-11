package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;

import java.util.List;

/**
 * Created by danilovey on 11.11.2016.
 */
@RestController
@RequestMapping(value = "/dc")
public class DomainController {

    @Autowired
    private RoleLDAP roleLDAP;

    @UrlDeclaration(description = "Получить все роли из DC.")
    @RequestMapping(value = "/get/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GeneralRolesEntity> getAllRolesFromDC() {
        return this.roleLDAP.findAllRoles();
    }

}

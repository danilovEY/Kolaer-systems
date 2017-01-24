package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;

import java.util.List;

/**
 * Created by danilovey on 11.11.2016.
 */
@RestController
@RequestMapping(value = "/dc")
@Api(tags = "Домен")
public class DomainController extends BaseController {

    @Autowired
    private RoleLDAP roleLDAP;

    @ApiOperation(
            value = "Получить все роли из DC",
            notes = "Получить все роли из DC"
    )
    @UrlDeclaration(description = "Получить все роли из DC.")
    @RequestMapping(value = "/get/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RoleEntity> getAllRolesFromDC() {
        return this.roleLDAP.findAllRoles();
    }

}

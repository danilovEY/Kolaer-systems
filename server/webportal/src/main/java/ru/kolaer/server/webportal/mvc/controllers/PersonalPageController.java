package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.PersonalPageDataDto;
import ru.kolaer.server.webportal.mvc.model.servirces.PersonalPageService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

/**
 * Created by danilovey on 11.11.2016.
 */
@RestController
@RequestMapping(value = "/personal-page")
@Api(tags = "Персональная страница", description = "Все данные для персональной станици")
public class PersonalPageController extends BaseController {

    @Autowired
    private PersonalPageService personalPageService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Получить данные для персональной страницы"
    )
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @UrlDeclaration(description = "Получить данные для персональной страници", isAccessUser = true)
    public PersonalPageDataDto getPersonalPageData() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new AuthenticationCredentialsNotFoundException("Вы не авторизовались!");
        return this.personalPageService.getPersonalPageData(serviceLDAP.getAccountWithEmployeeByLogin(authentication.getName()));
    }

}

package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.UserSessionInfo;
import ru.kolaer.server.webportal.mvc.model.entities.other.PersonalPageData;
import ru.kolaer.server.webportal.mvc.model.servirces.PersonalPageService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

/**
 * Created by danilovey on 11.11.2016.
 */
@RestController
@RequestMapping(value = "/personal_page")
public class PersonalPageController {

    @Autowired
    private PersonalPageService personalPageService;

    @Autowired
    private UserSessionInfo userSessionInfo;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @UrlDeclaration(description = "Получить данные для персональной страници", isAccessUser = true)
    public PersonalPageData getPersonalPageData() {
        return this.personalPageService.getPersonalPageData(this.userSessionInfo.getGeneralAccountsEntity());
    }

}

package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Рест контроллер для работы с url в БД.
 */
@RestController
@RequestMapping("/api")
public class UrlPathController {

    @Autowired
    private UrlPathDao urlPathDao;

    /**Получить все URL.*/
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<WebPortalUrlPathDecorator> getAllUrl() {
        return urlPathDao.findAll();
    }

}

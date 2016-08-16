package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.config.PathMapping;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.rss.WebPortalRssEntityDecorator;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Рест контроллер для работы с новострой лентой.
 */
@RestController
@RequestMapping(PathMapping.PATH_TO_RSS)
public class RssController {
    private static final Logger LOG = LoggerFactory.getLogger(RssController.class);

    @Autowired
    @Qualifier("rssDao")
    private RssDao rssDao;

    /**Получить все новости.*/
    @UrlDeclaration(description = "Получить все новости.", isAccessAll = true)
    @RequestMapping( value = PathMapping.PATH_TO_GET_RSS + PathMapping.PATH_TO_GET_ALL_RSS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WebPortalRssEntityDecorator> getAllRss() {
        List<WebPortalRssEntityDecorator> list = rssDao.findAll();
        return list;
    }

    /**Получить новость по id.*/
    @UrlDeclaration(description = "Получить новость по id.(?id={id})", isAccessAll = true)
    @RequestMapping( value = PathMapping.PATH_TO_GET_RSS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebPortalRssEntityDecorator getRssById(@RequestParam("id") String id) {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID in NULL!");
        } else {
            if(!id.matches("\\d+"))
                throw new IllegalArgumentException("ID in not integer!");
        }

        return rssDao.findByID(Short.valueOf(id));
    }
}

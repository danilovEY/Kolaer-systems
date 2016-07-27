package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.server.webportal.config.PathMapping;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.rss.WebPortalRssEntity;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
@RestController
@RequestMapping(PathMapping.PATH_TO_RSS)
public class RssController {
    private static final Logger LOG = LoggerFactory.getLogger(RssController.class);

    @Autowired
    @Qualifier("rssDao")
    private RssDao rssDao;

    @RequestMapping( value = PathMapping.PATH_TO_GET_RSS + PathMapping.PATH_TO_GET_ALL_RSS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    List<WebPortalRssEntity> getAllRss() {
        List<WebPortalRssEntity> list = rssDao.findAll();
        return list;
    }

    @RequestMapping( value = PathMapping.PATH_TO_GET_RSS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    WebPortalRssEntity getRssById(@RequestParam("id") String id) {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID in NULL!");
        } else {
            if(!id.matches("\\d+"))
                throw new IllegalArgumentException("ID in not integer!");
        }

        return rssDao.findByID(Short.valueOf(id));
    }
}

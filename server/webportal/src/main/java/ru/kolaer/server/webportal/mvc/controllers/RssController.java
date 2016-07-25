package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.rss.WebPortalRssEntity;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
@RestController
@RequestMapping("/rss")
public class RssController {
    private static final Logger LOG = LoggerFactory.getLogger(RssController.class);

    @Autowired
    @Qualifier("myRssDao")
    private RssDao rssDao;

    @RequestMapping( value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    List<WebPortalRssEntity> getAllRss() {
        return rssDao.findAll();
    }
}

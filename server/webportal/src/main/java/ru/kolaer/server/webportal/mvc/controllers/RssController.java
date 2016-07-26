package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.server.webportal.config.PathMapping;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.rss.WebPortalRssEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        return rssDao.findAll();
    }

    @RequestMapping( value = PathMapping.PATH_TO_GET_RSS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    WebPortalRssEntity getRssById(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
        if(id == null || id.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            if(!id.matches("\\d+"))
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return rssDao.findByID(Short.valueOf(id));
    }
}

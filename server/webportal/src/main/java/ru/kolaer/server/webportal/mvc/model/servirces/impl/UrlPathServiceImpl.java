package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class UrlPathServiceImpl implements UrlPathService {

    @Autowired
    private UrlPathDao urlPathDao;

    @Override
    public WebPortalUrlPath getPathByUrl(String url) {
        return this.urlPathDao.getPathByUrl(url);
    }

    @Override
    public List<WebPortalUrlPath> getAll() {
        return urlPathDao.findAll();
    }

    @Override
    public WebPortalUrlPath getById(Integer id) {
        return this.urlPathDao.findByID(id);
    }

    @Override
    public void add(WebPortalUrlPath entity) {
        this.urlPathDao.persist(entity);
    }
}

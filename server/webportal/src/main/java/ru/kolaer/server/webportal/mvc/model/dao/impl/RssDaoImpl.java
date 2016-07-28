package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.rss.WebPortalRssEntity;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
@Repository(value = "rssDao")
public class RssDaoImpl implements RssDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<WebPortalRssEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(WebPortalRssEntity.class).list();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebPortalRssEntity findByID(int id) {
        final  WebPortalRssEntity webPortalRssEntity = sessionFactory.getCurrentSession().get(WebPortalRssEntity.class,id);
        return webPortalRssEntity;
    }

    @Override
    public void save(WebPortalRssEntity obj) {

    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.rss.WebPortalRssEntity;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
@Repository(value = "myRssDao")
public class RssDaoImpl implements RssDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<WebPortalRssEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(WebPortalRssEntity.class).list();
    }

    @Override
    public WebPortalRssEntity findByID(Long id) {

        return null;
    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.RssDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.rss.WebPortalRssEntityDecorator;

import javax.validation.constraints.NotNull;
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
    public List<WebPortalRssEntityDecorator> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(WebPortalRssEntityDecorator.class).list();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebPortalRssEntityDecorator findByID(Integer id) {
        final WebPortalRssEntityDecorator webPortalRssEntity = sessionFactory.getCurrentSession().get(WebPortalRssEntityDecorator.class,id);
        return webPortalRssEntity;
    }

    @Override
    public void persist(WebPortalRssEntityDecorator obj) {

    }

    @Override
    public void delete(WebPortalRssEntityDecorator obj) {

    }

    @Override
    public void delete(List<WebPortalRssEntityDecorator> objs) {

    }

    @Override
    public void update(WebPortalRssEntityDecorator entity) {

    }

    @Override
    public void update(List<WebPortalRssEntityDecorator> objs) {

    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

import java.util.List;

@Repository("urlPathDao")
public class UrlPathDaoImpl implements UrlPathDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional(readOnly = true)
    public List<WebPortalUrlPath> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery("FROM WebPortalUrlPathDecorator ORDER BY url").list();
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPathDecorator findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(WebPortalUrlPathDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(WebPortalUrlPath obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPath getPathByUrl(String url) {
        return (WebPortalUrlPath) this.sessionFactory.getCurrentSession()
                .createQuery("FROM WebPortalUrlPathDecorator u WHERE u.url LIKE :url").setParameter("url", "%"+ url +"%").uniqueResult();
    }
}

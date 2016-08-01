package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

import java.util.List;

@Repository("urlPathDao")
public class UrlPathDaoImpl implements UrlPathDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional(readOnly = true)
    public List<WebPortalUrlPathDecorator> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(WebPortalUrlPathDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPathDecorator findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(WebPortalUrlPathDecorator.class, id);
    }

    @Override
    @Transactional
    public void save(WebPortalUrlPathDecorator obj) {
        this.sessionFactory.getCurrentSession().save(obj);
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPathDecorator getPathByUrl(String url) {
        final Query query = this.sessionFactory.getCurrentSession().createQuery("from WebPortalUrlPathDecorator u where u.url=:url");
        query.setParameter("url", url).uniqueResult();
        return null;
    }
}

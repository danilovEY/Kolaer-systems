package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

import java.util.Collection;
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
    public void delete(WebPortalUrlPath obj) {

    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPath getPathByUrl(String url) {
        return (WebPortalUrlPath) this.sessionFactory.getCurrentSession()
                .createQuery("FROM WebPortalUrlPathDecorator u WHERE u.url LIKE :url").setParameter("url", "%"+ url +"%").setFirstResult(0)
                .setMaxResults(1).uniqueResult();
    }

    @Override
    @Transactional
    public void update(WebPortalUrlPath webPortalUrlPath) {
        this.sessionFactory.getCurrentSession().update(webPortalUrlPath);
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPath getPathByUrlAndMethod(String url, String requestMethod) {
        return (WebPortalUrlPath) this.sessionFactory.getCurrentSession()
                .createQuery("FROM WebPortalUrlPathDecorator path WHERE path.url = :url AND path.requestMethod = :requestMethod")
                .setParameter("url", url)
                .setParameter("requestMethod", requestMethod)
                .uniqueResult();
    }

    @Override
    @Transactional
    public void clear() {
        this.sessionFactory.getCurrentSession().createQuery("DELETE FROM WebPortalUrlPathDecorator").executeUpdate();
    }

    @Override
    @Transactional
    public void removeAll(Collection<WebPortalUrlPath> values) {
        for (WebPortalUrlPath value : values) {
            this.sessionFactory.getCurrentSession().delete(value);
        }

    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPath;

import java.util.List;

@Repository("urlPathDao")
public class UrlPathDaoImpl implements UrlPathDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional(readOnly = true)
    public List<WebPortalUrlPath> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(WebPortalUrlPath.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPath findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(WebPortalUrlPath.class, id);
    }

    @Override
    @Transactional
    public void save(WebPortalUrlPath obj) {
        this.sessionFactory.getCurrentSession().save(obj);
    }

    @Override
    @Transactional(readOnly = true)
    public WebPortalUrlPath getPathByUrl(String url) {
        final Query query = this.sessionFactory.getCurrentSession().createQuery("from WebPortalUrlPath u where u.url=:url");
        query.setString("url", url);
        List<WebPortalUrlPath> list = query.list();
        if(list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPath;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 */
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
}

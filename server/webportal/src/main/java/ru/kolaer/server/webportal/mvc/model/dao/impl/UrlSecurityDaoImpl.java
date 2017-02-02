package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.server.webportal.mvc.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityDecorator;

import java.util.Collection;
import java.util.List;

@Repository("urlPathDao")
public class UrlSecurityDaoImpl implements UrlSecurityDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional(readOnly = true)
    public List<UrlSecurity> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery("FROM UrlSecurityDecorator ORDER BY url").list();
    }

    @Override
    @Transactional(readOnly = true)
    public UrlSecurityDecorator findByPersonnelNumber(Integer id) {
        return this.sessionFactory.getCurrentSession().get(UrlSecurityDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(UrlSecurity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public void delete(UrlSecurity obj) {

    }

    @Override
    public void delete(List<UrlSecurity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public UrlSecurity getPathByUrl(String url) {
        return (UrlSecurity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM UrlSecurityDecorator u WHERE u.url LIKE :url").setParameter("url", "%"+ url +"%").setFirstResult(0)
                .setMaxResults(1).uniqueResult();
    }

    @Override
    @Transactional
    public void update(UrlSecurity urlSecurity) {
        this.sessionFactory.getCurrentSession().update(urlSecurity);
    }

    @Override
    public void update(List<UrlSecurity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public UrlSecurity getPathByUrlAndMethod(String url, String requestMethod) {
        return (UrlSecurity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM UrlSecurityDecorator path WHERE path.url = :url AND path.requestMethod = :requestMethod")
                .setParameter("url", url)
                .setParameter("requestMethod", requestMethod)
                .uniqueResult();
    }

    @Override
    @Transactional
    public void clear() {
        this.sessionFactory.getCurrentSession().createQuery("DELETE FROM UrlSecurityDecorator").executeUpdate();
    }

    @Override
    @Transactional
    public void removeAll(Collection<UrlSecurity> values) {
        for (UrlSecurity value : values) {
            this.sessionFactory.getCurrentSession().delete(value);
        }

    }
}

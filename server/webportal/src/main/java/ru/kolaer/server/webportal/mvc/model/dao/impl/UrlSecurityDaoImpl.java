package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

@Repository("urlPathDao")
public class UrlSecurityDaoImpl extends AbstractDefaultDao<UrlSecurityEntity> implements UrlSecurityDao {

    protected UrlSecurityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, UrlSecurityEntity.class);
    }

    @Override
    public UrlSecurityEntity getPathByUrl(String url) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " u WHERE u.url LIKE :url", getEntityClass())
                .setParameter("url", "%"+ url +"%")
                .setFirstResult(0)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public UrlSecurityEntity getPathByUrlAndMethod(String url, String requestMethod) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " path WHERE path.url = :url AND path.requestMethod = :requestMethod", getEntityClass())
                .setParameter("url", url)
                .setParameter("requestMethod", requestMethod)
                .uniqueResult();
    }

    @Override
    public void clear() {
        getSession()
                .createQuery("DELETE FROM " + getEntityName())
                .executeUpdate();
    }
}

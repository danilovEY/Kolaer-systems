package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

@Repository
public class UrlSecurityDaoImpl extends AbstractDefaultDao<UrlSecurityEntity> implements UrlSecurityDao {

    protected UrlSecurityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, UrlSecurityEntity.class);
    }

    @Override
    public UrlSecurityEntity findPathByUrlAndMethod(String url, String requestMethod) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " u WHERE u.url LIKE :url AND u.requestMethod = :method", getEntityClass())
                .setParameter("url", "%"+ url +"%")
                .setParameter("method", requestMethod)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public String findAccessByUrlAndMethod(String url, String requestMethod) {
        return getSession()
                .createQuery("SELECT access FROM " + getEntityName() + " path WHERE path.url = :url AND path.requestMethod = :requestMethod", String.class)
                .setParameter("url", url)
                .setParameter("requestMethod", requestMethod)
                .uniqueResult();
    }
}

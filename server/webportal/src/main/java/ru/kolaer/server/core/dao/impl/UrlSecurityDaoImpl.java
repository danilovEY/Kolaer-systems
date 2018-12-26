package ru.kolaer.server.core.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.core.dao.UrlSecurityDao;
import ru.kolaer.server.core.model.entity.general.UrlSecurityEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class UrlSecurityDaoImpl extends AbstractDefaultDao<UrlSecurityEntity> implements UrlSecurityDao {

    protected UrlSecurityDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, UrlSecurityEntity.class);
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

    @Override
    public List<UrlSecurityEntity> findPathByMethod(String method) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " u WHERE u.requestMethod = :method", getEntityClass())
                .setParameter("method", method)
                .list();
    }
}

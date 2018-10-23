package ru.kolaer.server.service.account.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.common.entities.general.UrlSecurityEntity;

import java.util.List;

@Repository
public class UrlSecurityRepositoryImpl extends AbstractDefaultRepository<UrlSecurityEntity> implements UrlSecurityRepository {

    protected UrlSecurityRepositoryImpl(SessionFactory sessionFactory) {
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

    @Override
    public List<UrlSecurityEntity> findPathByMethod(String method) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " u WHERE u.requestMethod = :method", getEntityClass())
                .setParameter("method", method)
                .list();
    }
}

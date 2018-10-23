package ru.kolaer.server.service.storage.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.service.storage.entity.UploadFileEntity;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;

@Repository
public class UploadFileRepositoryImpl extends AbstractDefaultRepository<UploadFileEntity> implements UploadFileRepository {

    protected UploadFileRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, UploadFileEntity.class);
    }

    @Override
    public UploadFileEntity findByPath(String path) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " WHERE path = :path", getEntityClass())
                .setParameter("path", path)
                .uniqueResultOptional()
                .orElse(null);
    }
}

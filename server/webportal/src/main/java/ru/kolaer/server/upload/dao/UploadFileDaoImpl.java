package ru.kolaer.server.upload.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.upload.model.entity.UploadFileEntity;

import javax.persistence.EntityManagerFactory;

@Repository
public class UploadFileDaoImpl extends AbstractDefaultDao<UploadFileEntity> implements UploadFileDao {

    protected UploadFileDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, UploadFileEntity.class);
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

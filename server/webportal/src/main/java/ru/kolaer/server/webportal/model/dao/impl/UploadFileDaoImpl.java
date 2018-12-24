package ru.kolaer.server.webportal.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.UploadFileDao;
import ru.kolaer.server.webportal.model.entity.upload.UploadFileEntity;

@Repository
public class UploadFileDaoImpl extends AbstractDefaultDao<UploadFileEntity> implements UploadFileDao {

    protected UploadFileDaoImpl(SessionFactory sessionFactory) {
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

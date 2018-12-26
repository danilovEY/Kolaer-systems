package ru.kolaer.server.upload.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.upload.model.entity.UploadFileEntity;

public interface UploadFileDao extends DefaultDao<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

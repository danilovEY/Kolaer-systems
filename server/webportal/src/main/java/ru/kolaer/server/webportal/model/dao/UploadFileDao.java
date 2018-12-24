package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.upload.UploadFileEntity;

public interface UploadFileDao extends DefaultDao<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

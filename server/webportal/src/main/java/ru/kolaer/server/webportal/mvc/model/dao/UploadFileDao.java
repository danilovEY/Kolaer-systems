package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;

public interface UploadFileDao extends DefaultDao<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

package ru.kolaer.server.service.storage.repository;

import ru.kolaer.server.service.storage.entity.UploadFileEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;

public interface UploadFileRepository extends DefaultRepository<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

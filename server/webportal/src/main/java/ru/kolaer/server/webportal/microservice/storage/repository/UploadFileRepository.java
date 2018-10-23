package ru.kolaer.server.webportal.microservice.storage.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.storage.entity.UploadFileEntity;

public interface UploadFileRepository extends DefaultRepository<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

package ru.kolaer.server.webportal.microservice.storage;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

public interface UploadFileRepository extends DefaultRepository<UploadFileEntity> {
    UploadFileEntity findByPath(String path);
}

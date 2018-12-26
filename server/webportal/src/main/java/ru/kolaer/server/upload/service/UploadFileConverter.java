package ru.kolaer.server.upload.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.core.model.dto.upload.UploadFileDto;
import ru.kolaer.server.upload.model.entity.UploadFileEntity;

public interface UploadFileConverter extends BaseConverter<UploadFileDto,UploadFileEntity> {

}

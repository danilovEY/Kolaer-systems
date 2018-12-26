package ru.kolaer.server.upload.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.upload.model.entity.UploadFileEntity;
import ru.kolaer.server.webportal.model.dto.upload.UploadFileDto;

public interface UploadFileConverter extends BaseConverter<UploadFileDto,UploadFileEntity> {

}

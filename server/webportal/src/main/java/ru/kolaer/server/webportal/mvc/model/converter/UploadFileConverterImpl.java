package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dto.UploadFileDto;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;

@Service
public class UploadFileConverterImpl implements UploadFileConverter {

    @Override
    public UploadFileEntity convertToModel(UploadFileDto dto) {
        if(dto == null) {
            return null;
        }

        UploadFileEntity uploadFileEntity = new UploadFileEntity();
        uploadFileEntity.setId(dto.getId());
        uploadFileEntity.setAccountId(dto.getAccountId());
        uploadFileEntity.setPath(dto.getPath());
        uploadFileEntity.setFileName(dto.getFileName());
        uploadFileEntity.setFileCreate(dto.getFileCreate());

        return uploadFileEntity;
    }

    @Override
    public UploadFileDto convertToDto(UploadFileEntity model) {
        return updateData(new UploadFileDto(), model);
    }

    @Override
    public UploadFileDto updateData(UploadFileDto oldDto, UploadFileEntity newModel) {
        if(newModel == null || oldDto == null) {
            return null;
        }

        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setId(newModel.getId());
        uploadFileDto.setAccountId(newModel.getAccountId());
        uploadFileDto.setPath(newModel.getPath());
        uploadFileDto.setFileName(newModel.getFileName());
        uploadFileDto.setFileCreate(newModel.getFileCreate());

        return uploadFileDto;
    }
}

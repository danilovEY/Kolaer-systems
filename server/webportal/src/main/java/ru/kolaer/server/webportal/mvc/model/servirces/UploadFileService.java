package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.webportal.mvc.model.dto.UploadFileDto;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;

public interface UploadFileService extends DefaultService<UploadFileDto> {
    UploadFileEntity store(String folder, MultipartFile file, boolean generateUniquiredFileName);
    UploadFileEntity createFile(String folder, String fileName, boolean generateUniquiredFileName);

    Resource loadFile(String filename);
}

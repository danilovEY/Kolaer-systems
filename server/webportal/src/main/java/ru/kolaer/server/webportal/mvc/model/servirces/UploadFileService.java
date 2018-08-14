package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.webportal.mvc.model.dto.upload.UploadFileDto;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;

import javax.servlet.http.HttpServletResponse;

public interface UploadFileService extends DefaultService<UploadFileDto> {
    UploadFileEntity store(String folder, MultipartFile file, boolean generateUniquiredFileName);
    UploadFileEntity createFile(String folder, String fileName, boolean generateUniquiredFileName);

    Resource loadFile(String filename);

    String getAbsolutePath(String path);
    String getAbsolutePath(UploadFileEntity uploadFileEntity);

    ResponseEntity loadFile(UploadFileDto uploadFileDto, HttpServletResponse response);

    ResponseEntity loadFile(Long id, String filename, HttpServletResponse response);

    long delete(Long attachmentId);
}

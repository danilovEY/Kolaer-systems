package ru.kolaer.server.webportal.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.webportal.model.dto.upload.UploadFileDto;

import javax.servlet.http.HttpServletResponse;

public interface UploadFileService extends DefaultService<UploadFileDto> {
    UploadFileDto store(String folder, MultipartFile file, boolean generateUniquiredFileName);

    UploadFileDto createTempFile(String fileName);
    UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName);
    UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName, boolean inDateFolder);
    UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName, boolean inDateFolder, boolean replaceFile, boolean saveFileInDb);

    Resource loadFile(String filename, boolean absolutePath);

    String getAbsolutePath(String path);
    String getAbsolutePath(UploadFileDto uploadFileEntity);

    ResponseEntity loadFile(UploadFileDto uploadFileDto, HttpServletResponse response);

    ResponseEntity loadFile(Long id, String filename, HttpServletResponse response);

    long delete(Long attachmentId);
}

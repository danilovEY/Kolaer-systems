package ru.kolaer.server.upload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.core.model.dto.upload.UploadFileDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.upload.dao.UploadFileDao;
import ru.kolaer.server.upload.model.entity.UploadFileEntity;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class UploadFileServiceImpl
        extends AbstractDefaultService<UploadFileDto, UploadFileEntity, UploadFileDao, UploadFileConverter>
        implements UploadFileService {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final Path rootLocation = Paths.get("upload");

    public UploadFileServiceImpl(UploadFileDao uploadFileDao, UploadFileConverter uploadFileConverter) {
        super(uploadFileDao, uploadFileConverter);
    }

    public UploadFileDto store(String folder, MultipartFile file, boolean generateUniquiredFileName) {
        try {
            File folderFile = rootLocation.resolve(folder).toFile();
            if(!folderFile.exists() && !folderFile.mkdirs()) {
                throw new RuntimeException("Не удалось создать папку: " + folder);
            }
            String newFileName = folder + "/";
            if(generateUniquiredFileName) {
                newFileName += new Date().getTime() + "_";
            }
            newFileName += file.getOriginalFilename();

            Files.copy(file.getInputStream(), this.rootLocation.resolve(newFileName),
                    StandardCopyOption.REPLACE_EXISTING);

            UploadFileEntity uploadFileEntity = new UploadFileEntity();
            uploadFileEntity.setFileCreate(LocalDateTime.now());
            uploadFileEntity.setFileName(file.getOriginalFilename());
            uploadFileEntity.setPath(newFileName);

            return defaultConverter.convertToDto(defaultEntityDao.persist(uploadFileEntity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public UploadFileDto createTempFile(String fileName) {
        return createFile("temp", fileName, true, false, false, false);
    }

    @Transactional
    @Override
    public UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName) {
        return createFile(folder, fileName, generateUniquiredFileName, true);
    }

    @Override
    @Transactional
    public UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName, boolean inDateFolder) {
       return createFile(folder, fileName, generateUniquiredFileName, inDateFolder, true, true);
    }

    @Override
    @Transactional
    public UploadFileDto createFile(String folder, String fileName, boolean generateUniquiredFileName, boolean inDateFolder, boolean replaceFile, boolean saveFileInDb) {
        try {
            if(inDateFolder) {
                folder += "/" + simpleDateFormat.format(new Date());
            }

            File folderFile = rootLocation.resolve(folder).toFile();
            if(!folderFile.exists() && !folderFile.mkdirs()) {
                throw new RuntimeException("Не удалось создать папку: " + folder);
            }

            String newFileName = folder + "/";

            if(generateUniquiredFileName) {
                newFileName += new Date().getTime() + "_";
            }
            newFileName += fileName;

            File file = this.rootLocation.resolve(newFileName).toFile();

            if(file.exists()) {
                if (replaceFile && !file.delete()) {
                    newFileName = new Date().getTime() + "_" + newFileName;
                    file = this.rootLocation.resolve(newFileName).toFile();
                } else {
                    return null;
                }
            }

            UploadFileEntity uploadFileEntity = saveFileInDb
                    ? Optional.ofNullable(this.defaultEntityDao.findByPath(newFileName))
                    .orElse(new UploadFileEntity())
                    :  new UploadFileEntity();

            if (!file.exists() && file.createNewFile()) {
                uploadFileEntity.setFileCreate(LocalDateTime.now());
                uploadFileEntity.setFileName(fileName);
                uploadFileEntity.setPath(newFileName);

                if (saveFileInDb) {
                    uploadFileEntity = this.defaultEntityDao.save(uploadFileEntity);
                }
            }

            return defaultConverter.convertToDto(uploadFileEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadFile(String filename, boolean absolutePath) {
        try {
            URI uri = absolutePath ? new File(filename).toURI() : rootLocation.resolve(filename).toUri();

            Resource resource = new UrlResource(uri);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не найден или его не открыть: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAbsolutePath(String path) {
        return this.rootLocation.resolve(path).toAbsolutePath().toString();
    }

    @Override
    public String getAbsolutePath(UploadFileDto uploadFileEntity) {
        return uploadFileEntity.isAbsolutePath()
                ? uploadFileEntity.getPath()
                : this.getAbsolutePath(uploadFileEntity.getPath());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity loadFile(UploadFileDto uploadFileDto, HttpServletResponse response) {
        try {
            if(uploadFileDto != null) {
                Resource resource = loadFile(uploadFileDto.getPath(), uploadFileDto.isAbsolutePath());

                String mimeType = Optional.ofNullable(URLConnection.guessContentTypeFromName(uploadFileDto.getPath()))
                        .orElse("application/octet-stream");

                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", "inline; filename=\"" +
                        MimeUtility.encodeWord(uploadFileDto.getFileName(), "UTF-8", "Q") +"\"");
                response.setContentLength(Long.valueOf(resource.contentLength()).intValue());

                FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());

                return ResponseEntity.ok(null);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity loadFile(Long id, String filename, HttpServletResponse response) {
        UploadFileDto uploadFileDto = getById(id);
        if (uploadFileDto.getFileName().equals(filename)) {
            return loadFile(uploadFileDto, response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long delete(Long attachmentId) {
        UploadFileEntity fileEntity = this.defaultEntityDao.findById(attachmentId);
        long delete = this.defaultEntityDao.delete(attachmentId);
        FileSystemUtils.deleteRecursively(rootLocation.resolve(fileEntity.getPath()).toFile());
        return delete;
    }

    @PostConstruct
    public void init() {
        try {
            if(!rootLocation.toFile().exists()) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!", e);
        }
    }
}

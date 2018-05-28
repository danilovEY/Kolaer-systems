package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.webportal.mvc.model.converter.UploadFileConverter;
import ru.kolaer.server.webportal.mvc.model.dao.UploadFileDao;
import ru.kolaer.server.webportal.mvc.model.dto.UploadFileDto;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;


@Service
@Slf4j
public class UploadFileServiceImpl
        extends AbstractDefaultService<UploadFileDto, UploadFileEntity, UploadFileDao, UploadFileConverter>
        implements UploadFileService {
    private final Path rootLocation = Paths.get("upload");

    public UploadFileServiceImpl(UploadFileDao uploadFileDao, UploadFileConverter uploadFileConverter) {
        super(uploadFileDao, uploadFileConverter);
    }

    public UploadFileEntity store(String folder, MultipartFile file, boolean generateUniquiredFileName) {
        try {
            String newFileName = folder + "/";
            if(generateUniquiredFileName) {
                newFileName += new Date().getTime() + "_";
            }
            newFileName += file.getOriginalFilename();

            Files.copy(file.getInputStream(), this.rootLocation.resolve(newFileName),
                    StandardCopyOption.REPLACE_EXISTING);

            UploadFileEntity uploadFileEntity = new UploadFileEntity();
            uploadFileEntity.setCreate(LocalDateTime.now());
            uploadFileEntity.setName(file.getOriginalFilename());
            uploadFileEntity.setPath(newFileName);

            return this.defaultEntityDao.persist(uploadFileEntity);
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Transactional
    public UploadFileEntity createFile(String folder, String fileName, boolean generateUniquiredFileName) {
        try {
            String newFileName = folder + "/";
            if(generateUniquiredFileName) {
                newFileName += new Date().getTime() + "_";
            }
            newFileName += fileName;

            File file = new File(newFileName);
            if (!file.exists() && file.createNewFile()) {
                UploadFileEntity uploadFileEntity = new UploadFileEntity();
                uploadFileEntity.setCreate(LocalDateTime.now());
                uploadFileEntity.setName(fileName);
                uploadFileEntity.setPath(newFileName);

                return this.defaultEntityDao.persist(uploadFileEntity);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
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

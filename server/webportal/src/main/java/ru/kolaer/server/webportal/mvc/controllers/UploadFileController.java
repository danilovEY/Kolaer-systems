package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.UploadFile;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by danilovey on 07.02.2017.
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadFileController {

    @Resource
    private Environment env;

    private final AuthenticationService authenticationService;

    @Autowired
    public UploadFileController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @UrlDeclaration(description = "Загрузить файл на сервер", isUser = true)
    @ApiOperation("Загрузить файл на сервер")
    public UploadFile uploadFile(@ApiParam("Файл") @RequestParam("file") MultipartFile file) throws IOException {
        final String pathToResources = env.getProperty("path.to_resources");
        final String urlToResources = env.getProperty("url.to_resources");

        final String userName = this.authenticationService.getAccountByAuthentication().getUsername();

        String resourceDirPath = pathToResources + userName;

        final File resourceDir = new File(resourceDirPath);
        resourceDir.mkdirs();

        final String filePath = resourceDirPath + "\\" + file.getOriginalFilename();

        try(final FileOutputStream fileToResource = new FileOutputStream(filePath)) {
            fileToResource.write(file.getBytes());
        } catch (IOException e) {
            log.error("Невозможно записать в файл {}", filePath, e);
            throw e;
        }

        return new UploadFile(urlToResources + userName + '/' + file.getOriginalFilename(),
                file.getSize(), file.getOriginalFilename());
    }
}

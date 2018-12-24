package ru.kolaer.server.webportal.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.annotation.UrlDeclaration;
import ru.kolaer.server.webportal.service.UploadFileService;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 07.02.2017.
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadFileController {

    private final UploadFileService uploadFileService;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

//    @RequestMapping(value = "/file", method = RequestMethod.POST)
//    @UrlDeclaration(description = "Загрузить файл на сервер", isUser = true)
//    @ApiOperation("Загрузить файл на сервер")
//    public UploadFile uploadFile(@ApiParam("Файл") @RequestParam("file") MultipartFile file) throws IOException {
//        final String pathToResources = env.getProperty("path.to_resources");
//        final String urlToResources = env.getProperty("url.to_resources");
//
//        final String userName = this.authenticationService.getAccountByAuthentication().getUsername();
//
//        String resourceDirPath = pathToResources + userName;
//
//        final File resourceDir = new File(resourceDirPath);
//        resourceDir.mkdirs();
//
//        final String filePath = resourceDirPath + "\\" + file.getOriginalFilename();
//
//        try(final FileOutputStream fileToResource = new FileOutputStream(filePath)) {
//            fileToResource.write(file.getBytes());
//        } catch (IOException e) {
//            log.error("Невозможно записать в файл {}", filePath, e);
//            throw e;
//        }
//
//        return new UploadFile(urlToResources + userName + '/' + file.getOriginalFilename(),
//                file.getSize(), file.getOriginalFilename());
//    }

    @RequestMapping(value = "/file/{id}/{filename:.+}", method = RequestMethod.GET)
    @UrlDeclaration(description = "Скачать файл с сервера", isAccessAll = true)
    @ApiOperation("Скачать файл с сервера")
    public ResponseEntity getFile(@PathVariable("id") Long id,
                                  @PathVariable("filename") String filename,
                                  HttpServletResponse response) {
        return uploadFileService.loadFile(id, filename, response);
    }
}

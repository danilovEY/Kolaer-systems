package ru.kolaer.server.webportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.webportal.annotation.UrlDeclaration;
import ru.kolaer.server.webportal.model.servirce.UrlSecurityService;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Рест контроллер для работы с url в БД.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "URL пути", description = "Описание URL: путь и доступы.")
public class UrlSecurityController {

    private final UrlSecurityService urlSecurityService;

    @Autowired
    public UrlSecurityController(UrlSecurityService urlSecurityService) {
        this.urlSecurityService = urlSecurityService;
    }

    @ApiOperation(
            value = "Получить все URL"
    )
    @UrlDeclaration(description = "Получить все URL")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UrlSecurityDto> getAllUrl() {
        return urlSecurityService.getAll();
    }

    @ApiOperation(
            value = "Получить роли по URL"
    )
    @UrlDeclaration(description = "Получить роли по URL")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UrlSecurityDto getRoleByUrl(
            @ApiParam(value = "Путь от корня", required = true) @RequestParam(value = "path") String path,
            @ApiParam(value = "Метод", required = true) @RequestParam(value = "method") String method) {
        if(path == null || path.isEmpty())
            throw new IllegalArgumentException("Path is null!");

        if(path.indexOf("/") != 0)
            path = "/" + path;

        if(path.indexOf("/rest") != 0)
            path = "/rest" + path;

        return this.urlSecurityService.getPathByUrlAndMethod(path, method);
    }

}

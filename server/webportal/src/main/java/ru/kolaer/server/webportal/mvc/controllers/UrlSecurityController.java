package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Рест контроллер для работы с url в БД.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "URL пути", description = "Описание URL: путь и доступы.")
public class UrlSecurityController extends BaseController {

    @Autowired
    private UrlSecurityService urlSecurityService;

    @ApiOperation(
            value = "Получить все URL"
    )
    @UrlDeclaration(description = "Получить все URL")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UrlSecurity> getAllUrl() {
        return urlSecurityService.getAll();
    }

    @ApiOperation(
            value = "Получить роли по URL"
    )
    @UrlDeclaration(description = "Получить роли по URL")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UrlSecurity getRoleByUrl(
            @ApiParam(value = "Путь от корня", required = true) @RequestParam(value = "path") String path) {
        if(path == null || path.isEmpty())
            throw new IllegalArgumentException("Path is null!");

        if(path.indexOf("/") != 0)
            path = "/" + path;

        if(path.indexOf("/rest") != 0)
            path = "/rest" + path;

        return this.urlSecurityService.getPathByUrl(path);
    }

}

package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartamentService;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Подразделения")
@RestController
@RequestMapping(value = "/general/departament")
public class DepartamentController extends BaseController {

    @Autowired
    private DepartamentService departamentService;

    @ApiOperation(value = "Получить все подразделеия")
    @UrlDeclaration(description = "Получить все подразделения", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GeneralDepartamentEntity> getAllDepartament() {
        return this.departamentService.getAll();
    }



}

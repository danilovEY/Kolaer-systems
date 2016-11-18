package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrRegisterService;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrStatusService;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@RestController
@RequestMapping(value = "/psr")
@Api(description = "Работа с ПСР-проектами.", tags = "ПСР-проект")
public class PsrRegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(PsrRegisterController.class);

    @Autowired
    private PsrRegisterService psrRegisterService;

    @Autowired
    private PsrStatusService psrStatusService;

    @Autowired
    private EmployeeService employeeService;

    /**Получить все статусы.*/
    @ApiOperation(
            value = "Получить все статусы.",
            notes = "Получить все возможные статусы/положения для ПСР-проектов.",
            response = PsrStatusDecorator[].class
    )
    @UrlDeclaration(description = "Получить все статусы.", isAccessAll = true)
    @RequestMapping(value = "/status/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrStatus> getAllPsrStatus() {
        List<PsrStatus> list = this.psrStatusService.getAll();
        return list;
    }

    /**Получить все ПСР-проекты.*/
    @ApiOperation(
            value = "Получить все ПСР-проекты.",
            notes = "Получить все ПСР-проекты",
            response = PsrRegisterDecorator[].class
    )
    @UrlDeclaration(description = "Получить все ПСР-проекты.", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegister() {
        List<PsrRegister> list = this.psrRegisterService.getAll();
        return list;
    }

    /**Добавить ПСР-проект.*/
    @ApiOperation(
            value = "Добавить ПСР-проект.",
            notes = "Добавить ПСР-проект.",
            response = PsrRegisterDecorator.class
    )
    @UrlDeclaration(description = "Добавить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);

        final PsrStatus psrStatus = new PsrStatusDecorator();
        psrStatus.setId(1);
        psrStatus.setType("Новый");
        registerDto.setStatus(psrStatus);

        registerDto.setAuthor(this.employeeService.getById(register.getAuthor().getPnumber()));

        this.psrRegisterService.add(registerDto);
        return this.psrRegisterService.getPsrRegisterByName(registerDto.getName());
    }

    /**Добавить ПСР-проект.*/
    @ApiOperation(
            value = "Удалить ПСР-проект.",
            notes = "Удалить ПСР-проект."
    )
    @UrlDeclaration(description = "Удалить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        this.psrRegisterService.delete(registerDto);
    }

    /**Обновить ПСР-проект.*/
    @ApiOperation(
            value = "Обновить ПСР-проект.",
            notes = "Обновить ПСР-проект."
    )
    @UrlDeclaration(description = "Обновить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updatePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        this.psrRegisterService.update(registerDto);
    }

    /**Получить все ПСР-проект. (только id и имя).*/
    @ApiOperation(
            value = "Получить все ПСР-проект.",
            notes = "Только id и имя.",
            response = PsrRegisterDecorator[].class
    )
    @UrlDeclaration(description = "Получить все ПСР-проект. (только id и имя).", isAccessAll = true)
    @RequestMapping(value = "/get/all/id-name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegisterWithIdAndName() {
        List<PsrRegister> list = this.psrRegisterService.getIdAndNamePsrRegisters();
        return list;
    }
}

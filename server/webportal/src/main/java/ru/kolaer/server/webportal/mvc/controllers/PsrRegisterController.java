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
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrRegisterService;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrStatusService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import java.util.Date;
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
    private ServiceLDAP serviceLDAP;

    @ApiOperation(
            value = "Получить все статусы.",
            notes = "Получить все возможные статусы/положения для ПСР-проектов."
    )
    @UrlDeclaration(description = "Получить все статусы.", isAccessAll = true)
    @RequestMapping(value = "/status/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrStatus> getAllPsrStatus() {
        List<PsrStatus> list = this.psrStatusService.getAll();
        return list;
    }

    @ApiOperation(
            value = "Обновить статус у проекта.",
            notes = "Обновить статус у ПСР-проекта."
    )
    @UrlDeclaration(description = "Обновить статус у проекта", isAccessUser = true)
    @RequestMapping(value = "/psr/update/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister updatePsrRegisterStatus(@ApiParam(value = "ПСР-проект", required = true) PsrRegister psrRegister) {
        if(psrRegister.getId() == null || psrRegister.getId() < 0)
            throw new BadRequestException("ID null или меньше нуля!");
        if(psrRegister.getStatus() != null || psrRegister.getStatus().getType() == null)
            throw new BadRequestException("Статус или тип = null!");

        final PsrRegister updatePsrRegister = this.psrRegisterService.getById(psrRegister.getId());
        updatePsrRegister.setStatus(this.psrStatusService.getStatusByType(updatePsrRegister.getStatus().getType()));

        switch (updatePsrRegister.getStatus().getType()){
            case "Новый": { updatePsrRegister.setDateOpen(new Date()); break;}
            case "Закрыт": { updatePsrRegister.setDateClose(new Date()); break;}
            default: break;
        }

        this.psrRegisterService.update(updatePsrRegister);
        return updatePsrRegister;
    }

    @ApiOperation(
            value = "Получить все ПСР-проекты.",
            notes = "Получить все ПСР-проекты"
    )
    @UrlDeclaration(description = "Получить все ПСР-проекты.", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegister() {
        List<PsrRegister> list = this.psrRegisterService.getAll();
        return list;
    }

    @ApiOperation(
            value = "Добавить ПСР-проект.",
            notes = "Добавить ПСР-проект."
    )
    @UrlDeclaration(description = "Добавить ПСР-проект.", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        registerDto.setStatus(this.psrStatusService.getStatusByType("Новый"));

        if(this.psrRegisterService.uniquePsrRegister(register)) {
            registerDto.setAuthor(serviceLDAP.getEmployeeByAuthentication());

            this.psrRegisterService.add(registerDto);
            return this.psrRegisterService.getLastInsertPsrRegister(register);
        } else {
            throw new BadRequestException("ПСР-проект не уникальный!");
        }
    }

    @ApiOperation(
            value = "Удалить ПСР-проект.",
            notes = "Удалить ПСР-проект."
    )
    @UrlDeclaration(description = "Удалить ПСР-проект.", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        this.psrRegisterService.deletePstRegister(register.getId());
    }

    @ApiOperation(
            value = "Удалить ПСР-проекты (Список).",
            notes = "Удалить ПСР-проекты."
    )
    @UrlDeclaration(description = "Удалить ПСР-проекты.", isAccessUser = true)
    @RequestMapping(value = "/delete/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegisterList(
            @ApiParam(value = "ПСР-проекты", required = true) @RequestBody List<PsrRegister> registers) {
        this.psrRegisterService.deletePstRegisterListById(registers);
    }

    @ApiOperation(
            value = "Обновить ПСР-проект.",
            notes = "Обновить ПСР-проект."
    )
    @UrlDeclaration(description = "Обновить ПСР-проект.", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updatePsrRegister(@ApiParam(value = "ПСР-проект", required = true) @RequestBody PsrRegister register) {
        final PsrRegister updatePsrRegister = this.psrRegisterService.getById(register.getId());
        updatePsrRegister.setName(register.getName());
        updatePsrRegister.setComment(register.getComment());
        this.psrRegisterService.update(updatePsrRegister);
    }

    @ApiOperation(
            value = "Получить все ПСР-проект (id, name).",
            notes = "Только id и имя."
    )
    @UrlDeclaration(description = "Получить все ПСР-проект. (только id и имя).", isAccessAll = true)
    @RequestMapping(value = "/get/all/id-name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegisterWithIdAndName() {
        List<PsrRegister> list = this.psrRegisterService.getIdAndNamePsrRegisters();
        return list;
    }
}

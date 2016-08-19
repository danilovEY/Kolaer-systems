package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStateDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.PsrRegisterService;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 29.07.2016.
 */
@RestController
@RequestMapping(value = "/psr")
public class PsrRegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(PsrRegisterController.class);

    @Autowired
    private PsrRegisterService psrRegisterService;

    @Autowired
    private EmployeeService employeeService;

    /**Получить все ПСР-проекты.*/
    @UrlDeclaration(description = "Получить все ПСР-проекты.", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegister() {
        List<PsrRegister> list = this.psrRegisterService.getAll();
        return list;
    }

    /**Добавить ПСР-проект.*/
    @UrlDeclaration(description = "Добавить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrRegister(@RequestBody PsrRegister register) {
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
    @UrlDeclaration(description = "Удалить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removePsrRegister(@RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        this.psrRegisterService.delete(registerDto);
    }

    /**Обновить ПСР-проект.*/
    @UrlDeclaration(description = "Обновить ПСР-проект.", isAccessAll = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updatePsrRegister(@RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        this.psrRegisterService.update(registerDto);
    }

    /**Получить все ПСР-проект. (только id и имя).*/
    @UrlDeclaration(description = "Получить все ПСР-проект. (только id и имя).", isAccessAll = true)
    @RequestMapping(value = "/get/all/id-name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegisterWithIdAndName() {
        List<PsrRegister> list = this.psrRegisterService.getIdAndNamePsrRegisters();
        return list;
    }
}

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
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterBase;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PsrRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrRegisterDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStateDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.psr.PsrStatusDecorator;

import java.util.ArrayList;
import java.util.Collections;
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
    @Qualifier("psrRegisterDao")
    private PsrRegisterDao psrRegisterDao;

    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegister() {
        List<PsrRegister> list = this.psrRegisterDao.findAll();
        return list;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PsrRegister addPsrRegister(@RequestBody PsrRegister register) {
        PsrRegister registerDto = new PsrRegisterDecorator(register);
        List<PsrState> states = new ArrayList<>();
        register.getStateList().forEach(state -> {
            state.setId(null);
            states.add(new PsrStateDecorator(state));
        });
        registerDto.setStateList(states);
        registerDto.setId(null);
        //List<PsrState> states = registerDto.getStateList().stream().map(PsrStateDecorator::new).collect(Collectors.toList());
        //registerDto.setAuthor(null);
        //registerDto.setStateList(new ArrayList<>());
        final PsrStatus psrStatus = new PsrStatusDecorator();
        psrStatus.setId(1);
        psrStatus.setType("Новый");
        registerDto.setStatus(psrStatus);

        registerDto.setAuthor(this.employeeDao.findByID(register.getAuthor().getPnumber()));

        //return registerDto;
        this.psrRegisterDao.persist(registerDto);
        return this.psrRegisterDao.getPsrRegisterByName(registerDto.getName());
    }

    @RequestMapping(value = "/get/all/id-name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PsrRegister> getAllRegisterWithIdAndName() {
        List<PsrRegister> list = this.psrRegisterDao.getIdAndNamePsrRegister();
        return list;
    }
}

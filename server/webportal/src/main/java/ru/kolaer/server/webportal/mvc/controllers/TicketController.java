package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Талоны")
@RestController
@RequestMapping(value = "/ticket")
public class TicketController {

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketRegister> getAllRegister() {
        return this.ticketRegisterService.getAll();
    }

    @ApiOperation(value = "Добавить реестр талонов")
    @UrlDeclaration(description = "Добавить реестр талонов", isAccessUser = true)
    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addTicketRegister(TicketRegister ticketRegister) {
        ticketRegister.setCreateRegister(new Date());

        ticketRegister.setDepartament(this.serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity().getDepartament());

        this.ticketRegisterService.add(ticketRegister);
    }

}

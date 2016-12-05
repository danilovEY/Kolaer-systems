package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.Ticket;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Талоны")
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов", isAccessUser = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketRegister> getAllRegister() {
        return this.ticketRegisterService.getAllByDepName(serviceLDAP.getAccountByAuthentication().getGeneralEmployeesEntity().getDepartament().getName());
    }

    @ApiOperation(value = "Добавить талон в реестр")
    @UrlDeclaration(description = "Добавить талон в реестр по ID", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegister addTicketToRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegister ticketRegister) {
        TicketRegister updateTicketRegister = this.ticketRegisterService.getById(ticketRegister.getId());
        List<Ticket> tickets = Optional.ofNullable(updateTicketRegister.getTickets()).orElse(new ArrayList<>());
        tickets.addAll(ticketRegister.getTickets().stream().map(ticket -> {
            ticket.setEmployee(employeeService.getById(ticket.getEmployee().getPnumber()));
            return ticket;
        }).collect(Collectors.toList()));
        updateTicketRegister.setTickets(tickets);
        this.ticketRegisterService.update(updateTicketRegister);
        return updateTicketRegister;
    }

    @ApiOperation(value = "Удалить реестр")
    @UrlDeclaration(description = "Удалить реестра", isAccessUser = true)
    @RequestMapping(value = "/register/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegister ticket) {
        this.ticketRegisterService.delete(ticket);
    }

    @ApiOperation(value = "Удалить талон из реестра")
    @UrlDeclaration(description = "Удалить талон из реестра", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicketFromRegister(@ApiParam(value = "ID талона", required = true) @RequestBody Ticket ticket) {
        this.ticketDao.delete(ticket);
    }

    @ApiOperation(value = "Добавить реестр талонов")
    @UrlDeclaration(description = "Добавить реестр талонов", isAccessUser = true)
    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegister addTicketRegister() {
        TicketRegister ticketRegister = new TicketRegister();
        ticketRegister.setCreateRegister(new Date());
        ticketRegister.setDepartament(this.serviceLDAP.getAccountByAuthentication()
                .getGeneralEmployeesEntity().getDepartament());

        this.ticketRegisterService.add(ticketRegister);
        return ticketRegister;
    }

    @ApiOperation(value = "Получить реестр талонов по id")
    @UrlDeclaration(description = "Получить реестр талонов по id", isAccessUser = true)
    @RequestMapping(value = "/register/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegister getTicketRegister(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Integer id) {
        return this.ticketRegisterService.getById(id);
    }

    @ApiOperation(value = "Получить талоны по id реестра")
    @UrlDeclaration(description = "Получить талоны по id реестра", isAccessUser = true)
    @RequestMapping(value = "/get/by/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Ticket> getTickets(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Integer id) {
        return this.ticketRegisterService.getById(id).getTickets();
    }

}

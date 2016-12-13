package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
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
@Slf4j
public class TicketController {

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private ServiceLDAP serviceLDAP;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RegisterTicketScheduler registerTicketScheduler;

    @ApiOperation(value = "Сформировать отчет")
    @UrlDeclaration(description = "Сформировать отчет")
    @RequestMapping(value = "/generate/execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void generateAndMailSend() {
        registerTicketScheduler.setSend(true);
    }

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов", isAccessUser = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TicketRegister> getAllRegister(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                               @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return this.ticketRegisterService
                .getAllByDepName(number, pageSize,
                        serviceLDAP.getAccountByAuthentication()
                                .getGeneralEmployeesEntity().getDepartament().getName());
    }

    @ApiOperation(value = "Добавить талон в реестр")
    @UrlDeclaration(description = "Добавить талон в реестр по ID", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Ticket> addTicketToRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegister ticketRegister) {
        //Очищаем из запроса пустые объекты
        ticketRegister.setTickets(ticketRegister.getTickets().stream()
                .filter(ticket -> ticket.getEmployee() != null && ticket.getCount() != null)
                .collect(Collectors.toList()));

        TicketRegister updateTicketRegister = this.ticketRegisterService.getById(ticketRegister.getId());
        if(updateTicketRegister.isClose()) {
            throw new BadRequestException("Добавлять в реестр запрещено!");
        }

        List<Integer> pnumberUpdate = ticketRegister.getTickets().stream().map(ticket -> ticket.getEmployee().getPnumber()).collect(Collectors.toList());
        List<Ticket> tickets = Optional.ofNullable(updateTicketRegister.getTickets()).orElse(new ArrayList<>());

        List<Ticket> ticketsDoulbes = tickets.stream().filter(ticket ->
                pnumberUpdate.contains(ticket.getEmployee().getPnumber())).collect(Collectors.toList());

        if(ticketsDoulbes.size() > 0){
            final String initials = ticketsDoulbes.stream()
                    .map(ticket -> ticket.getEmployee().getInitials()).collect(Collectors.joining(", "));
            throw new BadRequestException("Найдены дубли: " + initials);
        }

        List<Ticket> ticketsToAdd = ticketRegister.getTickets().stream().map(ticket -> {
            ticket.setEmployee(employeeService.getById(ticket.getEmployee().getPnumber()));
            return ticket;
        }).collect(Collectors.toList());

        tickets.addAll(ticketsToAdd);
        updateTicketRegister.setTickets(tickets);
        this.ticketRegisterService.update(updateTicketRegister);
        return ticketsToAdd;
    }

    @ApiOperation(value = "Обновить талон")
    @UrlDeclaration(description = "Обновить талон по ID", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Ticket updateTicketToRegister(@ApiParam(value = "ID талона и данные", required = true) @RequestBody Ticket ticket) {
        Ticket updateTicket = this.ticketDao.findByID(ticket.getId());
        updateTicket.setCount(Optional.ofNullable(ticket.getCount()).orElse(updateTicket.getCount()));
        if(ticket.getEmployee() != null && !updateTicket.getEmployee().getPnumber().equals(ticket.getEmployee().getPnumber())) {
            updateTicket.setEmployee(this.employeeService.getById(ticket.getEmployee().getPnumber()));
        }
        this.ticketDao.update(updateTicket);
        return updateTicket;
    }

    @ApiOperation(value = "Удалить реестр")
    @UrlDeclaration(description = "Удалить реестра", isAccessUser = true)
    @RequestMapping(value = "/register/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegister ticket) {
        this.ticketRegisterService.delete(ticket);
    }

    @ApiOperation(value = "Удалить талоны из реестра")
    @UrlDeclaration(description = "Удалить талоны из реестра", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicketFromRegister(@ApiParam(value = "ID талонов", required = true) @RequestBody List<Ticket> tickets) {
        tickets.forEach(this.ticketDao::delete);
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

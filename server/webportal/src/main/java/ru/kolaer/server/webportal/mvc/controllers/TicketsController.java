package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.dto.TicketDto;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TicketsController {
    private final TicketRegisterService ticketRegisterService;
    private final TicketService ticketService;
    private final EmployeeService employeeService;
    private final RegisterTicketScheduler registerTicketScheduler;
    private final AuthenticationService authenticationService;

    public TicketsController(TicketRegisterService ticketRegisterService,
                             TicketService ticketService,
                             EmployeeService employeeService,
                             RegisterTicketScheduler registerTicketScheduler,
                             AuthenticationService authenticationService) {
        this.ticketRegisterService = ticketRegisterService;
        this.ticketService = ticketService;
        this.employeeService = employeeService;
        this.registerTicketScheduler = registerTicketScheduler;
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Сформировать отчет таймера")
    @UrlDeclaration(description = "Сформировать отчет таймера", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/generate/execute/scheduled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void generateAndMailSendScheduled() {
        registerTicketScheduler.generateZeroTicketsLastDayOfMonthScheduled();
    }

    @ApiOperation(value = "Сформировать отчет")
    @UrlDeclaration(description = "Сформировать отчет", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/generate/execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean generateAndMailSend() {
        return registerTicketScheduler.generateAddTicketDocument();
    }

//    @ApiOperation(value = "Сформировать отчет для всех счетов")
//    @UrlDeclaration(description = "Сформировать отчет для всех счетов", requestMethod = RequestMethod.POST)
//    @RequestMapping(value = "/generate/execute/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public boolean generateAllAndMailSend(@ApiParam("Кол-во талонов") @RequestParam("count") Integer count) {
//        return registerTicketScheduler
//                .generateSetTicketDocument(count, "IMMEDIATE", "DR", "Сформированные талоны ЛПП для зачисления для всех счетов. Файл во вложении!");
//    }
//
//    @ApiOperation(value = "Сформировать отчет для обнуления")
//    @UrlDeclaration(description = "Сформировать отчет для обнуления", requestMethod = RequestMethod.POST)
//    @RequestMapping(value = "/generate/execute/zero/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public boolean generateZeroAndMailSend() {
//        return registerTicketScheduler.generateZeroTicketDocument(allTickets);
//    }

    @ApiOperation(value = "Добавить е-майл")
    @UrlDeclaration(description = "Добавить е-майл")
    @RequestMapping(value = "/generate/add", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> addEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.addEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Удалить е-майл")
    @UrlDeclaration(description = "Удалить е-майл")
    @RequestMapping(value = "/generate/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> removeEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.removeEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить все е-майлы")
    @UrlDeclaration(description = "Получить все е-майлы")
    @RequestMapping(value = "/generate/emails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getEmails() {
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить последнюю дату отправки")
    @UrlDeclaration(description = "Получить последнюю дату отправки")
    @RequestMapping(value = "/generate/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getLastSend() {
        final LocalDateTime lastSend = registerTicketScheduler.getLastSend();
        return lastSend != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(lastSend) : "none";
    }

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов")
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TicketRegisterDto> getAllRegister(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                     @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        final AccountDto accountByAuthentication = authenticationService.getAccountByAuthentication();
        if(accountByAuthentication.isAccessOit()){
            return new Page<>(this.ticketRegisterService.getAll(), 0, 0, 0);
        } else {
            return this.ticketRegisterService.getAllByDepName(number, pageSize,
                            accountByAuthentication.getEmployee()
                                    .getDepartment().getName());
        }
    }

    @ApiOperation(value = "Добавить талон в реестр")
    @UrlDeclaration(description = "Добавить талон в реестр по ID")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketDto> addTicketToRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterDto ticketRegister) {
        //Очищаем из запроса пустые объекты
        ticketRegister.setTickets(ticketRegister.getTickets().stream()
                .filter(ticket -> ticket.getEmployee() != null && ticket.getCount() != null)
                .collect(Collectors.toList()));

        TicketRegisterDto updateTicketRegister = this.ticketRegisterService.getById(ticketRegister.getId());
        if(updateTicketRegister.isClose()) {
            throw new CustomHttpCodeException("Добавлять в реестр запрещено!", ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
        }

        List<Long> pnumberUpdate = ticketRegister.getTickets()
                .stream()
                .map(ticket -> ticket.getEmployee().getPersonnelNumber())
                .collect(Collectors.toList());

        List<TicketDto> tickets = Optional.ofNullable(updateTicketRegister.getTickets())
                .orElse(new ArrayList<>());

        List<TicketDto> ticketsDoulbes = tickets
                .stream()
                .filter(ticket -> pnumberUpdate.contains(ticket.getEmployee().getPersonnelNumber()))
                .collect(Collectors.toList());

        if(ticketsDoulbes.size() > 0){
            final String initials = ticketsDoulbes.stream()
                    .map(ticket -> ticket.getEmployee().getInitials()).collect(Collectors.joining(", "));
            throw new UnexpectedRequestParams("Найдены дубли: " + initials);
        }

        List<TicketDto> ticketsToAdd = ticketRegister.getTickets().stream().map(ticket -> {
            ticket.setEmployee(employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
            return ticket;
        }).collect(Collectors.toList());

        tickets.addAll(ticketsToAdd);
        updateTicketRegister.setTickets(tickets);
        this.ticketRegisterService.save(updateTicketRegister);
        return ticketsToAdd;
    }

    @ApiOperation(value = "Обновить талон")
    @UrlDeclaration(description = "Обновить талон по ID")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketDto updateTicketToRegister(@ApiParam(value = "ID талона и данные", required = true) @RequestBody TicketDto ticket) {
        TicketDto updateTicket = this.ticketService.getById(ticket.getId());
        updateTicket.setCount(Optional.ofNullable(ticket.getCount()).orElse(updateTicket.getCount()));
        if(ticket.getEmployee() != null && !updateTicket.getEmployee().getPersonnelNumber().equals(ticket.getEmployee().getPersonnelNumber())) {
            updateTicket.setEmployee(this.employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
        }
        this.ticketService.save(updateTicket);
        return updateTicket;
    }

    @ApiOperation(value = "Удалить реестр")
    @UrlDeclaration(description = "Удалить реестра")
    @RequestMapping(value = "/register/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterDto ticket) {
        this.ticketRegisterService.delete(ticket);
    }

    @ApiOperation(value = "Удалить талоны из реестра")
    @UrlDeclaration(description = "Удалить талоны из реестра")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicketFromRegister(@ApiParam(value = "ID талонов", required = true) @RequestBody List<TicketDto> tickets) {
        tickets.forEach(this.ticketService::delete);
    }

    @ApiOperation(value = "Добавить реестр талонов")
    @UrlDeclaration(description = "Добавить реестр талонов")
    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto addTicketRegister() {
        TicketRegisterDto ticketRegister = new TicketRegisterDto();
        ticketRegister.setCreateRegister(new Date());
        ticketRegister.setDepartment(authenticationService.getAccountByAuthentication()
                .getEmployee().getDepartment());

        this.ticketRegisterService.save(ticketRegister);
        return ticketRegister;
    }

    @ApiOperation(value = "Получить реестр талонов по ID")
    @UrlDeclaration(description = "Получить реестр талонов по ID")
    @RequestMapping(value = "/register/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto getTicketRegister(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Long id) {
        return this.ticketRegisterService.getById(id);
    }

    @ApiOperation(value = "Получить талоны по ID реестра")
    @UrlDeclaration(description = "Получить талоны по ID реестра")
    @RequestMapping(value = "/get/by/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketDto> getTickets(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Long id) {
        return this.ticketService.getTicketsByRegisterId(id);
    }
}
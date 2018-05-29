package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.mvc.model.dto.GenerateTicketRegister;
import ru.kolaer.server.webportal.mvc.model.dto.ReportTicketsConfig;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Талоны")
@RestController
@RequestMapping(value = "/tickets")
@Slf4j
public class TicketsController {
    private final TicketRegisterService ticketRegisterService;
    private final RegisterTicketScheduler registerTicketScheduler;

    public TicketsController(TicketRegisterService ticketRegisterService,
                             RegisterTicketScheduler registerTicketScheduler) {
        this.ticketRegisterService = ticketRegisterService;
        this.registerTicketScheduler = registerTicketScheduler;
    }

//    @ApiOperation(value = "Сформировать отчет таймера")
//    @UrlDeclaration(description = "Сформировать отчет таймера", requestMethod = RequestMethod.GET)
//    @RequestMapping(value = "/generate/execute/scheduled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public void generateAndMailSendScheduled() {
//        registerTicketScheduler.generateZeroTicketsLastDayOfMonthScheduled();
//    }

//    @ApiOperation(value = "Сформировать отчет реестра")
//    @UrlDeclaration(description = "Сформировать отчет реестра", requestMethod = RequestMethod.POST)
//    @RequestMapping(value = "{registerId}/report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public TicketRegisterDto generateAndMailSend(@PathVariable("registerId")Long registerId) {
//        return ticketRegisterService.generateReportByRegister(registerId);
//    }

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

//    @ApiOperation(value = "Получить последнюю дату отправки")
//    @UrlDeclaration(description = "Получить последнюю дату отправки")
//    @RequestMapping(value = "/generate/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public String getLastSend() {
//        final LocalDateTime lastSend = registerTicketScheduler.getLastSend();
//        return lastSend != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(lastSend) : "none";
//    }

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TicketRegisterDto> getAllRegister(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                     @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
            return this.ticketRegisterService.getAll(number, pageSize);
    }

    @ApiOperation(value = "Добавить все аккаунты в реестр")
    @UrlDeclaration(description = "Добавить все аккаунты в реестр", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/{regId}/full", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto addToRegisterAllAccounts(@PathVariable("regId") Long regId,
                                                      @RequestBody GenerateTicketRegister generateTicketRegister) {
        return this.ticketRegisterService.addToRegisterAllAccounts(regId, generateTicketRegister);
    }

    @ApiOperation(value = "Сгенерировать реестр с добавлением всех аккаунтов")
    @UrlDeclaration(description = "Сгенерировать реестр с добавлением всех аккаунтов", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/full", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateNewRegisterAndAddAllAccounts(@RequestBody GenerateTicketRegister generateTicketRegister) {
        return this.ticketRegisterService.createRegisterForAllAccounts(generateTicketRegister);
    }

    @ApiOperation(value = "Сгенерировать пустой реестр")
    @UrlDeclaration(description = "Сгенерировать пустой реестр", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/empty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateNewEmptyRegister() {
        return this.ticketRegisterService.createEmptyRegister();
    }

    @ApiOperation(value = "Сформировать отчет реестра")
    @UrlDeclaration(description = "Сформировать отчет реестра", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/{regId}/report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateReportForRegisterAndDownload(@PathVariable("regId") Long regId,
                                                    @RequestBody ReportTicketsConfig reportTicketsConfig,
                                                    HttpServletResponse response) {
        return this.ticketRegisterService.generateReportByRegisterAndDownload(regId, reportTicketsConfig, response);
    }

    @ApiOperation(value = "Сформировать отчет реестра и отправить")
    @UrlDeclaration(description = "Сформировать отчет реестра и отправить", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/{regId}/report/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateReportForRegisterAndSend(@PathVariable("regId") Long regId,
                                                    @RequestBody ReportTicketsConfig reportTicketsConfig) {
        return this.ticketRegisterService.generateReportByRegisterAndSend(regId, reportTicketsConfig);
    }

//    @ApiOperation(value = "Добавить талон в реестр")
//    @UrlDeclaration(description = "Добавить талон в реестр по ID")
//    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<TicketDto> addTicketToRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterDto ticketRegister) {
//        //Очищаем из запроса пустые объекты
//        ticketRegister.setTickets(ticketRegister.getTickets().stream()
//                .filter(ticket -> ticket.getEmployee() != null && ticket.getCount() != null)
//                .collect(Collectors.toList()));
//
//        TicketRegisterDto updateTicketRegister = this.ticketRegisterService.getById(ticketRegister.getId());
//        if(updateTicketRegister.isClose()) {
//            throw new CustomHttpCodeException("Добавлять в реестр запрещено!", ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
//        }
//
//        List<Long> pnumberUpdate = ticketRegister.getTickets()
//                .stream()
//                .map(ticket -> ticket.getEmployee().getPersonnelNumber())
//                .collect(Collectors.toList());
//
//        List<TicketDto> tickets = Optional.ofNullable(updateTicketRegister.getTickets())
//                .orElse(new ArrayList<>());
//
//        List<TicketDto> ticketsDoulbes = tickets
//                .stream()
//                .filter(ticket -> pnumberUpdate.contains(ticket.getEmployee().getPersonnelNumber()))
//                .collect(Collectors.toList());
//
//        if(ticketsDoulbes.size() > 0){
//            final String initials = ticketsDoulbes.stream()
//                    .map(ticket -> ticket.getEmployee().getInitials()).collect(Collectors.joining(", "));
//            throw new UnexpectedRequestParams("Найдены дубли: " + initials);
//        }
//
//        List<TicketDto> ticketsToAdd = ticketRegister.getTickets().stream().map(ticket -> {
//            ticket.setEmployee(employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
//            return ticket;
//        }).collect(Collectors.toList());
//
//        tickets.addAll(ticketsToAdd);
//        updateTicketRegister.setTickets(tickets);
//        this.ticketRegisterService.save(updateTicketRegister);
//        return ticketsToAdd;
//    }

//    @ApiOperation(value = "Обновить талон")
//    @UrlDeclaration(description = "Обновить талон по ID")
//    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public TicketDto updateTicketToRegister(@ApiParam(value = "ID талона и данные", required = true) @RequestBody TicketDto ticket) {
//        TicketDto updateTicket = this.ticketService.getById(ticket.getId());
//        updateTicket.setCount(Optional.ofNullable(ticket.getCount()).orElse(updateTicket.getCount()));
//        if(ticket.getEmployee() != null && !updateTicket.getEmployee().getPersonnelNumber().equals(ticket.getEmployee().getPersonnelNumber())) {
//            updateTicket.setEmployee(this.employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
//        }
//        this.ticketService.save(updateTicket);
//        return updateTicket;
//    }

//    @ApiOperation(value = "Удалить реестр")
//    @UrlDeclaration(description = "Удалить реестра")
//    @RequestMapping(value = "/register/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public void deleteRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterDto ticket) {
//        this.ticketRegisterService.delete(ticket);
//    }
//
//    @ApiOperation(value = "Удалить талоны из реестра")
//    @UrlDeclaration(description = "Удалить талоны из реестра")
//    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public void deleteTicketFromRegister(@ApiParam(value = "ID талонов", required = true) @RequestBody List<TicketDto> tickets) {
//        tickets.forEach(this.ticketService::delete);
//    }
//
//    @ApiOperation(value = "Добавить реестр талонов")
//    @UrlDeclaration(description = "Добавить реестр талонов")
//    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public TicketRegisterDto addTicketRegister() {
//        TicketRegisterDto ticketRegister = new TicketRegisterDto();
//        ticketRegister.setCreateRegister(new Date());
//        ticketRegister.setDepartment(authenticationService.getAccountByAuthentication()
//                .getEmployee().getDepartment());
//
//        this.ticketRegisterService.save(ticketRegister);
//        return ticketRegister;
//    }
//
//    @ApiOperation(value = "Получить реестр талонов по ID")
//    @UrlDeclaration(description = "Получить реестр талонов по ID")
//    @RequestMapping(value = "/register/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public TicketRegisterDto getTicketRegister(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Long id) {
//        return this.ticketRegisterService.getById(id);
//    }
//
//    @ApiOperation(value = "Получить талоны по ID реестра")
//    @UrlDeclaration(description = "Получить талоны по ID реестра")
//    @RequestMapping(value = "/get/by/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<TicketDto> getTickets(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Long id) {
//        return this.ticketService.getTicketsByRegisterId(id);
//    }
}
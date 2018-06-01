package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.mvc.model.dto.*;
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

    @ApiOperation(value = "Получить реестр по ID")
    @UrlDeclaration(description = "Получить реестр по ID", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/{regId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto getRegister(@PathVariable("regId") Long regId) {
        return this.ticketRegisterService.getById(regId);
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

    @ApiOperation(value = "Удалить реестр")
    @UrlDeclaration(description = "Удалить реестра", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/{regId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@PathVariable("regId") Long regId) {
        this.ticketRegisterService.delete(regId);
    }

    @ApiOperation(value = "Получить талоны по ID реестра")
    @UrlDeclaration(description = "Получить талоны по ID реестра")
    @RequestMapping(value = "/{regId}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TicketDto> getTickets(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                                      @RequestParam(value = "page", defaultValue = "0") Integer number,
                                      @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                      TicketSort sortParam,
                                      TicketFilter ticketFilter) {
        return this.ticketRegisterService.getTicketsByRegisterId(regId, number, pageSize, sortParam, ticketFilter);
    }

    @ApiOperation(value = "Добавить талон по ID реестра")
    @UrlDeclaration(description = "Добавить талон по ID реестра", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/{regId}/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketDto addTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                                     @RequestBody RequestTicketDto ticketDto) {
        return this.ticketRegisterService.addTicket(regId, ticketDto);
    }

    @ApiOperation(value = "Удалить талон по ID реестра")
    @UrlDeclaration(description = "Удалить талон по ID реестра", requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/{regId}/tickets/{ticketId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                             @ApiParam(value = "ID талона", required = true) @PathVariable("ticketId") Long ticketId) {
        this.ticketRegisterService.deleteTicket(regId, ticketId);
    }

    @ApiOperation(value = "Обновить талон по ID реестра")
    @UrlDeclaration(description = "Получить талон по ID реестра", requestMethod = RequestMethod.PUT)
    @RequestMapping(value = "/{regId}/tickets/{ticketId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketDto updateTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                               @ApiParam(value = "ID талона", required = true) @PathVariable("ticketId") Long ticketId,
                               @RequestBody RequestTicketDto ticketDto) {
        return this.ticketRegisterService.updateTicket(regId, ticketId, ticketDto);
    }
}
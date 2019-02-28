package ru.kolaer.server.ticket.collection;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.assess.TicketAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.core.bean.RegisterTicketScheduler;
import ru.kolaer.server.ticket.model.dto.RequestTicketDto;
import ru.kolaer.server.ticket.model.dto.TicketDto;
import ru.kolaer.server.ticket.model.dto.TicketRegisterDto;
import ru.kolaer.server.ticket.model.request.*;
import ru.kolaer.server.ticket.service.TicketRegisterService;

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
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/generate/add", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> addEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.addEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Удалить е-майл")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/generate/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> removeEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.removeEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить все е-майлы")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/generate/emails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getEmails() {
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить все реестры талонов")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<TicketRegisterDto> getAllRegister(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                                  RegisterTicketSort sortParam,
                                                  RegisterTicketFilter ticketFilter) {
            return this.ticketRegisterService.getAll(sortParam, ticketFilter, pageNum, pageSize);
    }

    @ApiOperation(value = "Добавить все аккаунты в реестр")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/{regId}/full", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto addToRegisterAllAccounts(@PathVariable("regId") Long regId,
                                                      @RequestBody GenerateTicketRegister generateTicketRegister) {
        return this.ticketRegisterService.addToRegisterAllAccounts(regId, generateTicketRegister);
    }

    @ApiOperation(value = "Получить реестр по ID")
    @PreAuthorize("isAuthenticated()") // TODO: add role
    @RequestMapping(value = "/{regId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto getRegister(@PathVariable("regId") Long regId) {
        return this.ticketRegisterService.getById(regId);
    }

    @ApiOperation(value = "Сгенерировать реестр с добавлением всех аккаунтов")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_FULL + "')")
    @RequestMapping(value = "/full", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateNewRegisterAndAddAllAccounts(@RequestBody GenerateTicketRegister generateTicketRegister) {
        return this.ticketRegisterService.createRegisterForAllAccounts(generateTicketRegister);
    }

    @ApiOperation(value = "Сгенерировать пустой реестр")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_WRITE + "')")
    @RequestMapping(value = "/empty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateNewEmptyRegister() {
        return this.ticketRegisterService.createEmptyRegister();
    }

    @ApiOperation(value = "Сформировать отчет реестра")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_REPORT + "')")
    @RequestMapping(value = "/{regId}/report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateReportForRegisterAndDownload(@PathVariable("regId") Long regId,
                                                    @RequestBody ReportTicketsConfig reportTicketsConfig,
                                                    HttpServletResponse response) {
        return this.ticketRegisterService.generateReportByRegisterAndDownload(regId, reportTicketsConfig, response);
    }

    @ApiOperation(value = "Сформировать отчет реестра и отправить")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_REPORT + "')")
    @RequestMapping(value = "/{regId}/report/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterDto generateReportForRegisterAndSend(@PathVariable("regId") Long regId,
                                                    @RequestBody ReportTicketsConfig reportTicketsConfig) {
        return this.ticketRegisterService.generateReportByRegisterAndSend(regId, reportTicketsConfig);
    }

    @ApiOperation(value = "Удалить реестр")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_WRITE + "')")
    @RequestMapping(value = "/{regId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@PathVariable("regId") Long regId) {
        this.ticketRegisterService.delete(regId);
    }

    @ApiOperation(value = "Получить талоны по ID реестра")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_READ + "')")
    @RequestMapping(value = "/{regId}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageDto<TicketDto> getTickets(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                      TicketSort sortParam,
                                      TicketFilter ticketFilter) {
        return this.ticketRegisterService.getTicketsByRegisterId(regId, pageNum, pageSize, sortParam, ticketFilter);
    }

    @ApiOperation(value = "Добавить талон по ID реестра")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_WRITE + "')")
    @RequestMapping(value = "/{regId}/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketDto addTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                                     @RequestBody RequestTicketDto ticketDto) {
        return this.ticketRegisterService.addTicket(regId, ticketDto);
    }

    @ApiOperation(value = "Удалить талон по ID реестра")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_WRITE + "')")
    @RequestMapping(value = "/{regId}/tickets/{ticketId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                             @ApiParam(value = "ID талона", required = true) @PathVariable("ticketId") Long ticketId) {
        this.ticketRegisterService.deleteTicket(regId, ticketId);
    }

    @ApiOperation(value = "Обновить талон по ID реестра")
    @PreAuthorize("hasRole('" + TicketAccessConstant.TICKET_REGISTER_WRITE + "')")
    @RequestMapping(value = "/{regId}/tickets/{ticketId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketDto updateTicket(@ApiParam(value = "ID реестра", required = true) @PathVariable("regId") Long regId,
                               @ApiParam(value = "ID талона", required = true) @PathVariable("ticketId") Long ticketId,
                               @RequestBody RequestTicketDto ticketDto) {
        return this.ticketRegisterService.updateTicket(regId, ticketId, ticketDto);
    }
}
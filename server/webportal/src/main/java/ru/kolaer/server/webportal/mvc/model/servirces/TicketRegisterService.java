package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterDto> {
    void generateZeroTicketsLastDayOfMonthScheduled();

    TicketRegisterDto addToRegisterAllAccounts(Long regId, GenerateTicketRegister generateTicketRegister);

    TicketRegisterDto createRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister);
    TicketRegisterDto createEmptyRegister();

    TicketRegisterDto generateReportByRegisterAndSend(Long registerId, ReportTicketsConfig config);
    TicketRegisterDto generateReportByRegisterAndDownload(Long registerId, ReportTicketsConfig config, HttpServletResponse response);

    long delete(Long regId);

    List<TicketDto> getTicketsByRegisterId(Long regId);

    TicketDto addTicket(Long regId, RequestTicketDto ticketDto);

    void deleteTicket(Long regId, Long ticketId);

    TicketDto updateTicket(Long regId, Long ticketId, RequestTicketDto ticketDto);
}

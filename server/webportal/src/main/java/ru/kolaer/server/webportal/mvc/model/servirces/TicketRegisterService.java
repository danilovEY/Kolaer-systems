package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.http.ResponseEntity;
import ru.kolaer.server.webportal.mvc.model.dto.GenerateTicketRegister;
import ru.kolaer.server.webportal.mvc.model.dto.ReportTicketsConfig;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterDto> {
    TicketRegisterDto generateRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister);

    void generateZeroTicketsLastDayOfMonthScheduled();

    TicketRegisterDto generateReportByRegisterAndSend(Long registerId, ReportTicketsConfig config);
    ResponseEntity generateReportByRegisterAndDownload(Long registerId, ReportTicketsConfig config, HttpServletResponse response);
}

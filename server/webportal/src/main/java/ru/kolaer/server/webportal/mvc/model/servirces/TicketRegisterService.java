package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.scheduling.annotation.Scheduled;
import ru.kolaer.server.webportal.mvc.model.dto.GenerateTicketRegister;
import ru.kolaer.server.webportal.mvc.model.dto.ReportTicketsConfig;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterDto> {
    @Scheduled(cron = "0 0 8 26-31 * ?", zone = "Europe/Moscow")
    void generateZeroTicketsLastDayOfMonthScheduled();

    TicketRegisterDto addToRegisterAllAccounts(Long regId, GenerateTicketRegister generateTicketRegister);

    TicketRegisterDto createRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister);
    TicketRegisterDto createEmptyRegister();

    TicketRegisterDto generateReportByRegisterAndSend(Long registerId, ReportTicketsConfig config);
    TicketRegisterDto generateReportByRegisterAndDownload(Long registerId, ReportTicketsConfig config, HttpServletResponse response);
}

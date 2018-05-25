package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dto.GenerateTicketRegister;
import ru.kolaer.server.webportal.mvc.model.dto.ReportTicketsConfig;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterDto> {
    @Transactional
    TicketRegisterDto generateRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister);

    TicketRegisterDto generateReportByRegister(Long registerId, ReportTicketsConfig config);
}

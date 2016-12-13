package ru.kolaer.server.webportal.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

/**
 * Created by danilovey on 13.12.2016.
 */
@Component
public class RegisterTicketScheduler {

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void generateTicketDocument() {

    }
}

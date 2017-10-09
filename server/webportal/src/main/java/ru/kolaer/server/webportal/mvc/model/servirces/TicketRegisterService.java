package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterEntity> {
    List<TicketRegisterEntity> getAllByDepName(String name);
    Page<TicketRegisterEntity> getAllByDepName(int number, int pageSize, String name);
    List<TicketRegisterEntity> getAllOpenRegister();
}

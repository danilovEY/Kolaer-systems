package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterDao extends DefaultDao<TicketRegisterEntity> {
    List<TicketRegisterEntity> findAllByDepName(String depName);
    Page<TicketRegisterEntity> findAllByDepName(int number, int pageSize, String depName);
    List<TicketRegisterEntity> getTicketRegisterByDateAndDep(Date date, String depName);

    List<TicketRegisterEntity> findAllOpenRegister();
}

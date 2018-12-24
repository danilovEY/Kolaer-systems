package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.ticket.TicketRegisterEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterDao extends DefaultDao<TicketRegisterEntity> {
    List<TicketRegisterEntity> findAllByDepName(String depName);
    List<TicketRegisterEntity> findAllByDepName(int number, int pageSize, String depName);

    Long findCountAllByDepName(int number, int pageSize, String depName);

    List<TicketRegisterEntity> getTicketRegisterByDateAndDep(Date date, String depName);

    List<TicketRegisterEntity> findAllOpenRegister();

    List<TicketRegisterEntity> findIncludeAllOnLastMonth();
}

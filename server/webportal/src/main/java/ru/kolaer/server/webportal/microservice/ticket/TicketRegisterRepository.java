package ru.kolaer.server.webportal.microservice.ticket;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterRepository extends DefaultRepository<TicketRegisterEntity> {
    List<TicketRegisterEntity> findAllByDepName(String depName);
    List<TicketRegisterEntity> findAllByDepName(int number, int pageSize, String depName);

    Long findCountAllByDepName(int number, int pageSize, String depName);

    List<TicketRegisterEntity> getTicketRegisterByDateAndDep(Date date, String depName);

    List<TicketRegisterEntity> findAllOpenRegister();

    List<TicketRegisterEntity> findIncludeAllOnLastMonth();
}

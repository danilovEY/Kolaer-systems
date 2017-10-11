package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends DefaultService<TicketRegisterDto> {
    List<TicketRegisterDto> getAllByDepName(String name);
    Page<TicketRegisterDto> getAllByDepName(int number, int pageSize, String name);
    List<TicketRegisterDto> getAllOpenRegister();
}

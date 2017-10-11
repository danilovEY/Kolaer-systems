package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class TicketRegisterDto implements BaseDto {
    private Long id;
    private boolean close;
    private List<TicketDto> tickets;
    private DepartmentDto department;
    private Date createRegister;
}

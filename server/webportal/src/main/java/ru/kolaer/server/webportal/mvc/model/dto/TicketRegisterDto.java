package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.Date;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class TicketRegisterDto implements BaseDto {
    private Long id;
    private boolean close;
    private Date createRegister;
    private Date sendRegisterTime;
}

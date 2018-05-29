package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class TicketRegisterDto implements BaseDto {
    private Long id;
    private boolean close;
    private LocalDateTime createRegister;
    private LocalDateTime sendRegisterTime;
    private UploadFileDto attachment;
}

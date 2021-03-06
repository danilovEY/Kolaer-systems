package ru.kolaer.server.ticket.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.server.core.model.dto.upload.UploadFileDto;

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
    private Long accountId;
}

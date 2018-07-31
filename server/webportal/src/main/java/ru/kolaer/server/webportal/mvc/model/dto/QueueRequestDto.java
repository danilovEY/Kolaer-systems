package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueType;

import java.time.LocalDateTime;

@Data
public class QueueRequestDto implements BaseDto {
    private Long id;
    private AccountSimpleDto account;
    private String comment;
    private LocalDateTime queueFrom;
    private LocalDateTime queueTo;
    private QueueType type;
}

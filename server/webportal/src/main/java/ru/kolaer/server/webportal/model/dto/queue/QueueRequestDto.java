package ru.kolaer.server.webportal.model.dto.queue;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.model.entity.queue.QueueType;

import java.time.LocalDateTime;

@Data
public class QueueRequestDto implements BaseDto {
    private Long id;
    private AccountDto account;
    private String comment;
    private LocalDateTime queueFrom;
    private LocalDateTime queueTo;
    private QueueType type;
}

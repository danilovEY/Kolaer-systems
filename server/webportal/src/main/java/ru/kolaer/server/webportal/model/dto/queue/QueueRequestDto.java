package ru.kolaer.server.webportal.model.dto.queue;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.server.queue.model.entity.QueueType;

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

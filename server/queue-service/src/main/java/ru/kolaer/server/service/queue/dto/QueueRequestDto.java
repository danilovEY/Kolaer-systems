package ru.kolaer.server.service.queue.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.service.queue.QueueType;

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

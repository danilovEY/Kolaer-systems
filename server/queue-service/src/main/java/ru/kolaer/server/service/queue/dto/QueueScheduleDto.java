package ru.kolaer.server.service.queue.dto;

import lombok.Data;

@Data
public class QueueScheduleDto {
    private QueueTargetDto target;
    private QueueRequestDto request;
}

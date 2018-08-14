package ru.kolaer.server.webportal.mvc.model.dto.queue;

import lombok.Data;

@Data
public class QueueScheduleDto {
    private QueueTargetDto target;
    private QueueRequestDto request;
}

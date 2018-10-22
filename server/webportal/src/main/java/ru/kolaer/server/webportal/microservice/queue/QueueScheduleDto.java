package ru.kolaer.server.webportal.microservice.queue;

import lombok.Data;

@Data
public class QueueScheduleDto {
    private QueueTargetDto target;
    private QueueRequestDto request;
}

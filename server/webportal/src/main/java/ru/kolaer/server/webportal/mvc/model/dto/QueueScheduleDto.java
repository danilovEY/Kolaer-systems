package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class QueueScheduleDto {
    private QueueTargetDto target;
    private QueueRequestDto request;
}

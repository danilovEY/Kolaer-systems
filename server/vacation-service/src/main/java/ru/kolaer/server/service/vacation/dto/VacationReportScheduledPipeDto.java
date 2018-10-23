package ru.kolaer.server.service.vacation.dto;

import lombok.Data;

@Data
public class VacationReportScheduledPipeDto {
    private String name;
    private long totalValue;
    private long value;
}

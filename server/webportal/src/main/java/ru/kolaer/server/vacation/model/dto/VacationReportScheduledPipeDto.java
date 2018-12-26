package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

@Data
public class VacationReportScheduledPipeDto {
    private String name;
    private long totalValue;
    private long value;
}

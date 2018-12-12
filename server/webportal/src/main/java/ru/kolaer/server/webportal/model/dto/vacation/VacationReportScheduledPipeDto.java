package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

@Data
public class VacationReportScheduledPipeDto {
    private String name;
    private long totalValue;
    private long value;
}

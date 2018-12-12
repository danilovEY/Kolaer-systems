package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarMonthDto {
    private String month;
    private List<VacationReportCalendarDayDto> days;
}

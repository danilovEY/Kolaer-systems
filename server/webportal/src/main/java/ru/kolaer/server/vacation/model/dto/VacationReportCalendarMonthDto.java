package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarMonthDto {
    private String month;
    private List<VacationReportCalendarDayDto> days;
}

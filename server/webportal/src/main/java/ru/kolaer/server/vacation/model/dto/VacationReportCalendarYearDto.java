package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarYearDto {
    private String year;
    private List<VacationReportCalendarMonthDto> months;
}

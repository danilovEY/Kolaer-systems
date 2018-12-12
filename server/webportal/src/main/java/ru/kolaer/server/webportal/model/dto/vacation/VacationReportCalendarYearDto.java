package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarYearDto {
    private String year;
    private List<VacationReportCalendarMonthDto> months;
}

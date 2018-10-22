package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarYearDto {
    private String year;
    private List<VacationReportCalendarMonthDto> months;
}

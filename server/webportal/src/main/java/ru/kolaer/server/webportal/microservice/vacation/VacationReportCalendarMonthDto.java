package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarMonthDto {
    private String month;
    private List<VacationReportCalendarDayDto> days;
}

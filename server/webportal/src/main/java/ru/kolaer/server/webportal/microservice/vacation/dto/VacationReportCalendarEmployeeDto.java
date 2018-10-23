package ru.kolaer.server.webportal.microservice.vacation.dto;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarEmployeeDto {
    private String employee;
    private List<VacationReportCalendarYearDto> years;
}

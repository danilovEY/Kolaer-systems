package ru.kolaer.server.service.vacation.dto;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarEmployeeDto {
    private String employee;
    private List<VacationReportCalendarYearDto> years;
}

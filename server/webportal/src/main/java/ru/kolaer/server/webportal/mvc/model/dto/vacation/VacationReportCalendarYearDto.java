package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.util.List;

@Data
public class VacationReportCalendarYearDto {
    private String year;
    private List<VacationReportCalendarMonthDto> months;
}

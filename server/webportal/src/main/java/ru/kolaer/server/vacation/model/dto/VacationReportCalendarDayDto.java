package ru.kolaer.server.vacation.model.dto;

import lombok.Data;

@Data
public class VacationReportCalendarDayDto {
    private String day;
    private String title;
    private boolean vacation;
    private boolean dayOff;
    private boolean holiday;
    private boolean counter;
}

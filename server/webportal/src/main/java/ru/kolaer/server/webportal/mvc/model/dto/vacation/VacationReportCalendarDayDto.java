package ru.kolaer.server.webportal.mvc.model.dto.vacation;

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

package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDto {
    private int days;
    private LocalDate from;
    private LocalDate to;
    private Long holidayDays;
}

package ru.kolaer.server.service.vacation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDto {
    private int days;
    private LocalDate from;
    private LocalDate to;
    private int holidayDays;
}

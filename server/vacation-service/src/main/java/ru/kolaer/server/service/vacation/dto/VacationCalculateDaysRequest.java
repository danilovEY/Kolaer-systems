package ru.kolaer.server.service.vacation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDaysRequest {
    private LocalDate from;
    private LocalDate to;
}

package ru.kolaer.server.vacation.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDaysRequest {
    private LocalDate from;
    private LocalDate to;
}

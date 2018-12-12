package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDaysRequest {
    private LocalDate from;
    private LocalDate to;
}

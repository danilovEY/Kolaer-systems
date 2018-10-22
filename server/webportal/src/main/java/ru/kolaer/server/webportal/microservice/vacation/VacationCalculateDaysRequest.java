package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDaysRequest {
    private LocalDate from;
    private LocalDate to;
}

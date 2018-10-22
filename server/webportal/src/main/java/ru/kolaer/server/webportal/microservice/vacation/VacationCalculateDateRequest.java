package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDateRequest {
    private LocalDate from;
    private int days;
}

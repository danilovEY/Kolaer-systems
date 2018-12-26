package ru.kolaer.server.vacation.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDateRequest {
    private LocalDate from;
    private int days;
}

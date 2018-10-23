package ru.kolaer.server.service.vacation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDateRequest {
    private LocalDate from;
    private int days;
}

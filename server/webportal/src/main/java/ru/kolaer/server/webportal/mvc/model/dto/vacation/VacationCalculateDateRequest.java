package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateDateRequest {
    private LocalDate from;
    private int days;
}

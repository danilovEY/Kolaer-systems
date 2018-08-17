package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationCalculateRequest {
    private LocalDate from;
    private LocalDate to;
}

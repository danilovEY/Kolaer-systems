package ru.kolaer.server.service.counter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindCounterRequest {
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean displayOnVacation;
}

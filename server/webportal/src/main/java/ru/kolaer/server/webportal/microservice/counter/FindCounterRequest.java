package ru.kolaer.server.webportal.microservice.counter;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindCounterRequest {
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean displayOnVacation;
}

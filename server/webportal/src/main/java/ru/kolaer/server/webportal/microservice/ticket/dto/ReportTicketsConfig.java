package ru.kolaer.server.webportal.microservice.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTicketsConfig {
    boolean immediate;
    LocalDateTime inTime;
}

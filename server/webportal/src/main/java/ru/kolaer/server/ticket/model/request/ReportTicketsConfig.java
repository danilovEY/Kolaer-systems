package ru.kolaer.server.ticket.model.request;

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

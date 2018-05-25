package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportTicketsConfig {
    boolean immediate;
    LocalDateTime inTime;
}

package ru.kolaer.server.service.vacation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
public class GenerateReportCalendarRequest {
    private List<Long> typeWorkIds = Collections.emptyList();
    private List<Long> postIds = Collections.emptyList();
    private List<Long> departmentIds = Collections.emptyList();
    private List<Long> employeeIds = Collections.emptyList();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}

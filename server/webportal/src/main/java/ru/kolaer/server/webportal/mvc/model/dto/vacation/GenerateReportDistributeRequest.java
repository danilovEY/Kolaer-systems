package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
public class GenerateReportDistributeRequest {
    private List<Long> departmentIds = Collections.emptyList();
    private boolean allDepartment;
    private boolean addPipes;
    private GenerateReportDistributeSplitType splitType = GenerateReportDistributeSplitType.MONTHS;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}

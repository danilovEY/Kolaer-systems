package ru.kolaer.server.webportal.microservice.vacation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
public class GenerateReportDistributeRequest {
    private Set<Long> typeWorkIds = Collections.emptySet();
    private Set<Long> postIds = Collections.emptySet();
    private Set<Long> departmentIds = Collections.emptySet();
    private Set<Long> employeeIds = Collections.emptySet();
    private boolean addOtherData;
    private boolean groupByDepartments;
    private boolean addPipesForVacation;
    private boolean calculateIntersections;
    private GenerateReportDistributeSplitType splitType = GenerateReportDistributeSplitType.MONTHS;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}

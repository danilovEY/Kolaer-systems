package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
public class FindVacationByDepartmentRequest {
    private Set<Long> typeWorkIds = Collections.emptySet();
    private Set<Long> postIds = Collections.emptySet();
    private Set<Long> departmentIds = Collections.emptySet();
    private Set<Long> employeeIds = Collections.emptySet();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}

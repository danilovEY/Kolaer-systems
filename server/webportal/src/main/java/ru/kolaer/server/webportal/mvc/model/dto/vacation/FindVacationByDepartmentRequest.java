package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
public class FindVacationByDepartmentRequest {
    private List<Long> typeWorkIds = Collections.emptyList();
    private List<Long> postIds = Collections.emptyList();
    private List<Long> departmentIds = Collections.emptyList();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}

package ru.kolaer.server.webportal.microservice.employee.dto;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class FindEmployeeByDepartment {
    private Set<Long> departmentIds = Collections.emptySet();
    private Set<Long> postIds = Collections.emptySet();
    private Set<Long> employeeIds = Collections.emptySet();
    private Set<Long> typeWorkIds = Collections.emptySet();
}

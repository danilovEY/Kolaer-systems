package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class FindEmployeeByDepartment {
    private List<Long> departmentIds = Collections.emptyList();
    private List<Long> postIds = Collections.emptyList();
    private List<Long> employeeIds = Collections.emptyList();
    private List<Long> typeWorkIds = Collections.emptyList();
}

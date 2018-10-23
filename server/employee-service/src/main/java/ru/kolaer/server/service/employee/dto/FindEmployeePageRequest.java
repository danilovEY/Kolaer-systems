package ru.kolaer.server.service.employee.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindEmployeePageRequest extends PaginationRequest {
    private Set<Long> typeWorkIds = Collections.emptySet();
    private Set<Long> postIds = Collections.emptySet();
    private Set<Long> departmentIds = Collections.emptySet();
    private Set<Long> employeeIds = Collections.emptySet();
    private String query;
    private boolean onOnePage;
    private EmployeeSortType sort = EmployeeSortType.INITIALS_ASC;
}

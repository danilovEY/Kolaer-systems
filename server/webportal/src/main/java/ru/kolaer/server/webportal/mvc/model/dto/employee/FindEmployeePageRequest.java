package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

@Data
public class FindEmployeePageRequest extends PaginationRequest {
    private Long departmentId;
    private EmployeeSortType sort = EmployeeSortType.INITIALS_ASC;
}

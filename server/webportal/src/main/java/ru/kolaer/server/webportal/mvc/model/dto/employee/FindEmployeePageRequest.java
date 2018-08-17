package ru.kolaer.server.webportal.mvc.model.dto.employee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindEmployeePageRequest extends PaginationRequest {
    private Long departmentId;
    private boolean onOnePage;
    private EmployeeSortType sort = EmployeeSortType.INITIALS_ASC;
}

package ru.kolaer.server.employee.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.dto.PaginationRequest;

import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindEmployeePageRequest extends PaginationRequest {
    private Set<Long> ids = Collections.emptySet();
    private Set<Long> typeWorkIds = Collections.emptySet();
    private Set<Long> postIds = Collections.emptySet();
    private Set<Long> departmentIds = Collections.emptySet();
    private Long findByPersonnelNumber;
    private String findByAll;
    private String findByInitials;
    private String findByDepartmentName;
    private String findByPostName;
    private Boolean findByDeleted;
    private Set<EmployeeSortType> sorts = Collections.singleton(EmployeeSortType.INITIALS_ASC);
}

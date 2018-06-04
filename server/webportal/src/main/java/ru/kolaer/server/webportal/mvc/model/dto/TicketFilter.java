package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

@Data
public class TicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    private FilterType typeFilterId = FilterType.EQUAL;

    @EntityFieldName(name = "registerId")
    private Long filterRegisterId;
    private FilterType typeFilterRegisterId = FilterType.EQUAL;

    @EntityFieldName(name = "count")
    private Integer filterCount;
    private FilterType typeFilterCount = FilterType.EQUAL;

    @EntityFieldName(name = "typeOperation")
    private TypeOperation filterTypeOperation;
    private FilterType typeFilterTypeOperation = FilterType.EQUAL;

    @EntityFieldName(name = "bankAccount.employee.initials")
    private String filterEmployeeInitials;
    @EntityFieldName(name = "bankAccount.employee.post.name")
    private String filterEmployeePost;
    @EntityFieldName(name = "bankAccount.employee.department.name")
    private String filterEmployeeDepartment;
}

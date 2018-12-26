package ru.kolaer.server.ticket.model.request;

import lombok.Data;
import ru.kolaer.server.ticket.model.TypeOperation;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.FilterType;

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
    private TypeOperation filterType;
    private FilterType typeFilterType = FilterType.EQUAL;

    @EntityFieldName(name = "bankAccount.employee.initials")
    private String filterEmployee;
    @EntityFieldName(name = "bankAccount.employee.post.name")
    private String filterEmployeePost;
    @EntityFieldName(name = "bankAccount.employee.department.name")
    private String filterEmployeeDepartment;
}

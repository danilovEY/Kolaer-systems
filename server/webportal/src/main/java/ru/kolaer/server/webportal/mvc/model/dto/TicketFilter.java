package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

@Data
public class TicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "registerId")
    private Long filterRegisterId;
    @EntityFieldName(name = "count")
    private Integer filterCount;
    @EntityFieldName(name = "typeOperation")
    private TypeOperation filterTypeOperation;
    @EntityFieldName(name = "bankAccount.employee.initials")
    private String filterEmployeeInitials;
    @EntityFieldName(name = "bankAccount.employee.post.name")
    private String filterEmployeePost;
    @EntityFieldName(name = "bankAccount.employee.department.name")
    private String filterEmployeeDepartment;
}

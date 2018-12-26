package ru.kolaer.server.ticket.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class TicketSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "count")
    private SortType sortCount;
    @EntityFieldName(name = "typeOperation")
    private SortType sortTypeOperation;
    @EntityFieldName(name = "bankAccount.employee.initials")
    private SortType sortEmployeeInitials;
    @EntityFieldName(name = "bankAccount.employee.post.name")
    private SortType sortEmployeePost;
    @EntityFieldName(name = "bankAccount.employee.department.name")
    private SortType sortEmployeeDepartment;
}

package ru.kolaer.server.ticket.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class BankAccountSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "check")
    private SortType sortCheck;
    @EntityFieldName(name = "employee.initials")
    private SortType sortEmployeeInitials;
    @EntityFieldName(name = "employee.post.name")
    private SortType sortEmployeePost;
    @EntityFieldName(name = "employee.department.name")
    private SortType sortEmployeeDepartment;
}

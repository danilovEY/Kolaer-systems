package ru.kolaer.server.webportal.mvc.model.dto.bank;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.SortParam;
import ru.kolaer.server.webportal.mvc.model.dto.SortType;

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

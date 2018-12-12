package ru.kolaer.server.webportal.model.dto.account;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.SortType;

@Data
public class AccountSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "employee.initials")
    private SortType sortInitials;
    @EntityFieldName(name = "employee.post.name")
    private SortType sortPostName;
    @EntityFieldName(name = "employee.department.name")
    private SortType sortDepartmentName;
}

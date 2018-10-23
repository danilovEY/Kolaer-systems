package ru.kolaer.server.webportal.microservice.account.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.dto.SortType;

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

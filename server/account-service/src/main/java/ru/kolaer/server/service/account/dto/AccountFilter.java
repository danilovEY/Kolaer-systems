package ru.kolaer.server.service.account.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;

@Data
public class AccountFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "employee.initials")
    private String filterInitials;
    @EntityFieldName(name = "employee.post.name")
    private String filterPostName;
    @EntityFieldName(name = "employee.department.name")
    private String filterDepartmentName;
}

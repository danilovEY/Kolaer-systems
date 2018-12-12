package ru.kolaer.server.webportal.model.dto.bank;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.FilterType;

@Data
public class BankAccountFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "check")
    private String filterCheck;
    @EntityFieldName(name = "deleted")
    private Boolean filterDeleted;
    private FilterType typeFilterDeleted = FilterType.EQUAL;
    @EntityFieldName(name = "employee.initials")
    private String filterEmployee;
    @EntityFieldName(name = "employee.post.name")
    private String filterEmployeePost;
    @EntityFieldName(name = "employee.department.name")
    private String filterEmployeeDepartment;
}

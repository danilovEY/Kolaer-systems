package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class BankAccountFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "check")
    private String filterCheck;
    @EntityFieldName(name = "employee.initials")
    private String filterEmployee;
    @EntityFieldName(name = "employee.post.name")
    private String filterEmployeePost;
    @EntityFieldName(name = "employee.department.name")
    private String filterEmployeeDepartment;
}

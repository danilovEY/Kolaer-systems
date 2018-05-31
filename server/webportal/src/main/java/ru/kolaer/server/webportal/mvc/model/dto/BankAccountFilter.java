package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class BankAccountFilter implements FilterParam {
    @FilterValueName()
    private Long id;
    @FilterValueName()
    private String check;
    @FilterValueName(name = "employee.initials")
    private String employeeInitials;
    @FilterValueName(name = "employee.post.name")
    private String employeePost;
    @FilterValueName(name = "employee.department.name")
    private String employeeDepartment;
}

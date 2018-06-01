package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

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

package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class AccountSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "username")
    private SortType sortInitials;
    @EntityFieldName(name = "post.name")
    private SortType sortPostName;
    @EntityFieldName(name = "department.name")
    private SortType sortDepartmentName;
}

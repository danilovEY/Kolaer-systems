package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class EmployeeFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "initials")
    private String filterInitials;
    @EntityFieldName(name = "deleted")
    private Boolean filterDeleted;
    private FilterType typeFilterDeleted = FilterType.EQUAL;
    @EntityFieldName(name = "post.name")
    private String filterPostName;
    @EntityFieldName(name = "department.name")
    private String filterDepartmentName;
}

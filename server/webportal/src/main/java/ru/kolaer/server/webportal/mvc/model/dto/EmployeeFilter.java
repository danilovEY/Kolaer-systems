package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class EmployeeFilter implements FilterParam {
    @FilterValueName()
    private Long id;
    @FilterValueName()
    private String initials;
    @FilterValueName(name = "post.name")
    private String postName;
    @FilterValueName(name = "department.name")
    private String departmentName;
}

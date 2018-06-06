package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class AccountFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "username")
    private String filterInitials;
    @EntityFieldName(name = "post.name")
    private String filterPostName;
    @EntityFieldName(name = "department.name")
    private String filterDepartmentName;
}

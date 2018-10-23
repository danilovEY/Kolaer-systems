package ru.kolaer.server.webportal.microservice.department.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.FilterType;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;

@Data
public class DepartmentFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    @EntityFieldName(name = "name")
    private String filterName;
    @EntityFieldName(name = "abbreviatedName")
    private String filterAbbreviatedName;
    @EntityFieldName(name = "deleted")
    private Boolean filterDeleted;
    private FilterType typeFilterDeleted = FilterType.EQUAL;
}

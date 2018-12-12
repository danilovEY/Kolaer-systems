package ru.kolaer.server.webportal.model.dto.department;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.FilterType;

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

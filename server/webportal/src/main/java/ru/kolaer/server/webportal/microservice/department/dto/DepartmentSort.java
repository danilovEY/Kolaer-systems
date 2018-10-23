package ru.kolaer.server.webportal.microservice.department.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.SortType;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;

@Data
public class DepartmentSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "abbreviatedName")
    private SortType sortAbbreviatedName;
    @EntityFieldName(name = "code")
    private SortType sortCode;
}

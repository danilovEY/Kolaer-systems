package ru.kolaer.server.employee.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class DepartmentSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "abbreviatedName")
    private SortType sortAbbreviatedName;
    @EntityFieldName(name = "code")
    private SortType sortCode;
}

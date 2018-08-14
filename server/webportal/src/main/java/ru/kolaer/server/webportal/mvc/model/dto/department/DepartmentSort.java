package ru.kolaer.server.webportal.mvc.model.dto.department;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.SortParam;
import ru.kolaer.server.webportal.mvc.model.dto.SortType;

@Data
public class DepartmentSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "abbreviatedName")
    private SortType sortAbbreviatedName;
    @EntityFieldName(name = "code")
    private SortType sortCode;
}

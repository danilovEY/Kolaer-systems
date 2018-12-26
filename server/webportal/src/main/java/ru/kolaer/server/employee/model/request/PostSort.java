package ru.kolaer.server.employee.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class PostSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;

    @EntityFieldName(name = "abbreviatedName")
    private SortType sortAbbreviatedName;

    @EntityFieldName(name = "rang")
    private SortType sortRang;

    @EntityFieldName(name = "type")
    private SortType sortType;

    @EntityFieldName(name = "name")
    private SortType sortName;
}

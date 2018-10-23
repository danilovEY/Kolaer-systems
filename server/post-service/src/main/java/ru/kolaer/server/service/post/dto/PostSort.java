package ru.kolaer.server.service.post.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.dto.SortType;

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

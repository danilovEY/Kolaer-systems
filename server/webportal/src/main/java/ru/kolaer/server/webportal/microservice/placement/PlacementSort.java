package ru.kolaer.server.webportal.microservice.placement;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.SortType;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;

@Data
public class PlacementSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "name")
    private SortType sortAbbreviatedName;
}

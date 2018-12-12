package ru.kolaer.server.webportal.model.dto.placement;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.SortType;

@Data
public class PlacementSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "name")
    private SortType sortAbbreviatedName;
}

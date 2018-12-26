package ru.kolaer.server.core.model.dto.placement;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class PlacementSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "name")
    private SortType sortAbbreviatedName;
}

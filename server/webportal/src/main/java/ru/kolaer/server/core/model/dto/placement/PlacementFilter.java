package ru.kolaer.server.core.model.dto.placement;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.FilterParam;

@Data
public class PlacementFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;

    @EntityFieldName(name = "name")
    private String filterName;
}

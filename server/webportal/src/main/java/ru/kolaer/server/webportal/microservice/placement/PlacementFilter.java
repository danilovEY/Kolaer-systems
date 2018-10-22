package ru.kolaer.server.webportal.microservice.placement;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;

@Data
public class PlacementFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;

    @EntityFieldName(name = "name")
    private String filterName;
}

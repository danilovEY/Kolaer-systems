package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class PlacementFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;

    @EntityFieldName(name = "name")
    private String filterName;
}

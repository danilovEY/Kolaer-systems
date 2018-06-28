package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class PlacementSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "name")
    private SortType sortAbbreviatedName;
}

package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class PostSort implements SortParam {
    @EntityFieldName(name = "id")
    private SortType sortId;
    @EntityFieldName(name = "abbreviatedName")
    private SortType sortAbbreviatedName;
}

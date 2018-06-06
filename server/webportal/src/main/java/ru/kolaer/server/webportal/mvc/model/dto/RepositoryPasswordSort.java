package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class RepositoryPasswordSort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;
    private SortType sortInitials;
}

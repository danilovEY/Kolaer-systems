package ru.kolaer.server.service.kolpass.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.dto.SortType;

@Data
public class RepositoryPasswordSort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;
    private SortType sortInitials;
}

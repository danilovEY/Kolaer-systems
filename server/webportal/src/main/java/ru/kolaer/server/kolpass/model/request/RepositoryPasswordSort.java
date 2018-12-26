package ru.kolaer.server.kolpass.model.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.SortType;

@Data
public class RepositoryPasswordSort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;
    private SortType sortInitials;
}

package ru.kolaer.server.webportal.mvc.model.dto.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.SortParam;
import ru.kolaer.server.webportal.mvc.model.dto.SortType;

@Data
public class RepositoryPasswordSort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;
    private SortType sortInitials;
}

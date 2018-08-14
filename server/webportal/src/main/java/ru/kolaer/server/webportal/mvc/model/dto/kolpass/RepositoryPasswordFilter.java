package ru.kolaer.server.webportal.mvc.model.dto.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.FilterParam;
import ru.kolaer.server.webportal.mvc.model.dto.FilterType;

import java.util.List;

@Data
public class RepositoryPasswordFilter implements FilterParam {
    @EntityFieldName(name = "name")
    private String filterName;

    @EntityFieldName(name = "accountId")
    private Long filterAccountId;
    private FilterType typeFilterAccountId = FilterType.EQUAL;

    @EntityFieldName(name = "id")
    private List<Long> filterIds;
    private FilterType typeFilterIds = FilterType.IN;

    private String filterInitials;
}
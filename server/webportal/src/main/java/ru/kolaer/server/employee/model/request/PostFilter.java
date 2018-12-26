package ru.kolaer.server.employee.model.request;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.TypePostEnum;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.FilterType;

@Data
public class PostFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;

    @EntityFieldName(name = "rang")
    private Integer filterRang;
    private FilterType typeFilterRang = FilterType.EQUAL;

    @EntityFieldName(name = "type")
    private TypePostEnum filterType;
    private FilterType typeFilterType = FilterType.EQUAL;

    @EntityFieldName(name = "abbreviatedName")
    private String filterAbbreviatedName;

    @EntityFieldName(name = "code")
    private String filterCode;

    @EntityFieldName(name = "name")
    private String filterName;

    @EntityFieldName(name = "deleted")
    private Boolean filterDeleted;
    private FilterType typeFilterDeleted = FilterType.EQUAL;


}
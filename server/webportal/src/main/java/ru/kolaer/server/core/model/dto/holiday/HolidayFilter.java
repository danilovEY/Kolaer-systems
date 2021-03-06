package ru.kolaer.server.core.model.dto.holiday;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.TypeDay;
import ru.kolaer.server.core.model.dto.EntityFieldName;
import ru.kolaer.server.core.model.dto.FilterParam;
import ru.kolaer.server.core.model.dto.FilterType;

import java.time.LocalDate;

@Data
public class HolidayFilter implements FilterParam {

    @EntityFieldName(name = "name")
    private String filterName;

    @EntityFieldName(name = "holidayDate")
    private LocalDate filterHolidayDate;
    private FilterType typeFilterHolidayDate;

    @EntityFieldName(name = "holidayType")
    private TypeDay filterHolidayType;
    private FilterType typeFilterHolidayType = FilterType.EQUAL;

}

package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;

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

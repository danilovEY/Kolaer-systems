package ru.kolaer.server.webportal.microservice.holiday.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;
import ru.kolaer.server.webportal.common.dto.FilterType;

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

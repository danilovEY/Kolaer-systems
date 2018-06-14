package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class HolidaySort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;

    @EntityFieldName(name = "holidayDate")
    private SortType sortHolidayDate = SortType.DESC;

    @EntityFieldName(name = "holidayType")
    private SortType sortHolidayType;
}

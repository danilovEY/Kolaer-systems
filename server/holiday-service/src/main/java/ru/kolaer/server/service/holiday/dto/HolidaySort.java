package ru.kolaer.server.service.holiday.dto;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.SortParam;
import ru.kolaer.server.webportal.common.dto.SortType;

@Data
public class HolidaySort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;

    @EntityFieldName(name = "holidayDate")
    private SortType sortHolidayDate = SortType.DESC;

    @EntityFieldName(name = "holidayType")
    private SortType sortHolidayType;
}

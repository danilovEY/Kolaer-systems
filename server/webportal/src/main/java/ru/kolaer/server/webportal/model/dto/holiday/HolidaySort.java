package ru.kolaer.server.webportal.model.dto.holiday;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.SortParam;
import ru.kolaer.server.webportal.model.dto.SortType;

@Data
public class HolidaySort implements SortParam {
    @EntityFieldName(name = "name")
    private SortType sortName;

    @EntityFieldName(name = "holidayDate")
    private SortType sortHolidayDate = SortType.DESC;

    @EntityFieldName(name = "holidayType")
    private SortType sortHolidayType;
}

package ru.kolaer.server.webportal.model.dto.holiday;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;

import java.time.LocalDate;
import java.util.List;

@Data
public class FindHolidayRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<TypeDay> typeHolidays;
}

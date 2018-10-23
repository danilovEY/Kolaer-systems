package ru.kolaer.server.webportal.microservice.holiday.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.TypeDay;

import java.time.LocalDate;
import java.util.List;

@Data
public class FindHolidayRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<TypeDay> typeHolidays;
}

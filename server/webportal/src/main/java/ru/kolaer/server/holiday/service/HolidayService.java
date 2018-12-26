package ru.kolaer.server.holiday.service;

import ru.kolaer.common.dto.kolaerweb.DateTimeJson;
import ru.kolaer.common.dto.kolaerweb.Holiday;
import ru.kolaer.server.core.model.dto.holiday.HolidayDto;
import ru.kolaer.server.core.service.DefaultService;

import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
public interface HolidayService extends DefaultService<HolidayDto> {
    List<Holiday> getAllHolidays();
    Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson);
    List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson);

    HolidayDto update(Long id, HolidayDto holidayDto);

    List<HolidayDto> getAllByYear(int year);
}

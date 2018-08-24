package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.mvc.model.dto.holiday.HolidayDto;

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

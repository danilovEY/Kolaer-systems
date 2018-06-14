package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.mvc.model.entities.holiday.HolidayEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
public interface HolidayDao extends DefaultDao<HolidayEntity> {
    List<Holiday> getAllHolidays();
    Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson);
    List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson);
    void insertHolidays(List<Holiday> holidays);

    HolidayEntity findByDate(LocalDate holidayDate);
}

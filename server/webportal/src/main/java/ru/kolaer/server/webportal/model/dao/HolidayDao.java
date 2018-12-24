package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.common.dto.kolaerweb.DateTimeJson;
import ru.kolaer.common.dto.kolaerweb.Holiday;
import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.model.entity.holiday.HolidayEntity;

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

    List<HolidayEntity> findAll(FindHolidayRequest findHolidayRequest);

    Long findCountAll(FindHolidayRequest findHolidayRequest);
}

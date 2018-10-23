package ru.kolaer.server.webportal.microservice.holiday.repository;

import ru.kolaer.common.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.common.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.holiday.dto.FindHolidayRequest;
import ru.kolaer.server.webportal.microservice.holiday.entity.HolidayEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
public interface HolidayRepository extends DefaultRepository<HolidayEntity> {
    List<Holiday> getAllHolidays();
    Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson);
    List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson);
    void insertHolidays(List<Holiday> holidays);

    HolidayEntity findByDate(LocalDate holidayDate);

    List<HolidayEntity> findAll(FindHolidayRequest findHolidayRequest);

    Long findCountAll(FindHolidayRequest findHolidayRequest);
}

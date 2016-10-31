package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.mvc.model.dao.HolidayDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 31.10.2016.
 */
@Repository(value = "holidayDao")
public class HolidayDaoImpl implements HolidayDao {
    private final List<Holiday> holidays = new ArrayList<>();

    @Override
    public List<Holiday> getAllHolidays() {
        return this.holidays;
    }

    @Override
    public Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson) {
        final String[] tempDate = dateTimeJson.getDate().split("-");
        final Integer day = Integer.valueOf(tempDate[0]);
        final Integer month = Integer.valueOf(tempDate[1]);

        for(Holiday holiday : this.holidays){
            final String[] holidayDate = holiday.getDateTimeHoliday().getDate().split("-");
            final Integer holidayDay = Integer.valueOf(holidayDate[0]);
            final Integer holidayMonth = Integer.valueOf(holidayDate[1]);
            if(holidayDay.equals(day) && holidayMonth.equals(month))
                return holiday;

        }
        return null;
    }

    @Override
    public List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson) {
        final String[] tempDate = dateTimeJson.getDate().split("-");
        final Integer month = Integer.valueOf(tempDate[1]);

        return this.holidays.stream().filter(holiday -> {
            final String[] holidayDate = holiday.getDateTimeHoliday().getDate().split("-");
            final Integer holidayMonth = Integer.valueOf(holidayDate[1]);
            return holidayMonth.equals(month);
        }).collect(Collectors.toList());
    }

    @Override
    public void insertHolidays(List<Holiday> holidays) {
        this.holidays.clear();
        this.holidays.addAll(Optional.of(holidays).orElse(Collections.emptyList()));
    }
}

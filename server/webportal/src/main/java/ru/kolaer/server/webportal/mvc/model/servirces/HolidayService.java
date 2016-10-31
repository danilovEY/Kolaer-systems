package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;

import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
public interface HolidayService {
    List<Holiday> getAllHolidays();
    Holiday getHolidayByDayMonth(DateTimeJson dateTimeJson);
    List<Holiday> getHolidayByMonth(DateTimeJson dateTimeJson);
}

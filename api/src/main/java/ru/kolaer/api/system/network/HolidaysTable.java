package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.Holiday;

/**
 * Created by danilovey on 02.11.2016.
 */
public interface HolidaysTable {
    Holiday[] getHolidaysInThisMonth();
    Holiday[] getHolidays(int month, int year);
    Holiday[] getHolidaysAll();
}

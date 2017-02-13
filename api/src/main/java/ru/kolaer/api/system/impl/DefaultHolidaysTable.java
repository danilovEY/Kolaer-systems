package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.system.network.HolidaysTable;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultHolidaysTable implements HolidaysTable {
    @Override
    public Holiday[] getHolidaysInThisMonth() {
        return new Holiday[0];
    }

    @Override
    public Holiday[] getHolidays(int month, int year) {
        return new Holiday[0];
    }

    @Override
    public Holiday[] getHolidaysAll() {
        return new Holiday[0];
    }
}

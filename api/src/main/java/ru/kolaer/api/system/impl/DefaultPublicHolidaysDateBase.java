package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.restful.PublicHolidays;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultPublicHolidaysDateBase implements PublicHolidaysDateBase {
    @Override
    public PublicHolidays[] getPublicHolidaysInThisMonth() {
        return new PublicHolidays[0];
    }

    @Override
    public PublicHolidays[] getPublicHolidays(int month, int year) {
        return new PublicHolidays[0];
    }

    @Override
    public PublicHolidays[] getPublicHolidaysAll() {
        return new PublicHolidays[0];
    }
}

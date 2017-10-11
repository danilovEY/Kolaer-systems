package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

import java.util.List;

/**
 * Created by danilovey on 02.11.2016.
 */
public interface HolidaysTable {
    ServerResponse<List<Holiday>> getHolidaysInThisMonth();
    ServerResponse<List<Holiday>> getHolidays(int month, int year);
    ServerResponse<List<Holiday>> getHolidaysAll();
}

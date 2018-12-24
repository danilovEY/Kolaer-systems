package ru.kolaer.common.system.network;

import ru.kolaer.common.dto.kolaerweb.Holiday;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.util.List;

/**
 * Created by danilovey on 02.11.2016.
 */
public interface HolidaysTable {
    ServerResponse<List<Holiday>> getHolidaysInThisMonth();
    ServerResponse<List<Holiday>> getHolidays(int month, int year);
    ServerResponse<List<Holiday>> getHolidaysAll();
}

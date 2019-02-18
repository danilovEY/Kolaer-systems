package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.network.HolidaysTable;
import ru.kolaer.common.dto.kolaerweb.Holiday;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultHolidaysTable implements HolidaysTable {

    @Override
    public ServerResponse<List<Holiday>> getHolidaysInThisMonth() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<Holiday>> getHolidays(int month, int year) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<Holiday>> getHolidaysAll() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }
}

package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.other.PublicHolidays;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;

import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultPublicHolidaysDateBase implements PublicHolidaysDateBase {

    @Override
    public ServerResponse<List<PublicHolidays>> getPublicHolidaysInThisMonth() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<PublicHolidays>> getPublicHolidays(int month, int year) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<PublicHolidays>> getPublicHolidaysAll() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }
}

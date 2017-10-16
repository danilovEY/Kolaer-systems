package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.network.HolidaysTable;
import ru.kolaer.api.system.network.OtherPublicAPI;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultOtherPublicAPI implements OtherPublicAPI {
    private final HolidaysTable holidaysTable = new DefaultHolidaysTable();

    @Override
    public HolidaysTable getHolidaysTable() {
        return this.holidaysTable;
    }
}

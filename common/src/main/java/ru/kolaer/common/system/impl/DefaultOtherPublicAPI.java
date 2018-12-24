package ru.kolaer.common.system.impl;

import ru.kolaer.common.system.network.HolidaysTable;
import ru.kolaer.common.system.network.KaesTable;
import ru.kolaer.common.system.network.OtherPublicAPI;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultOtherPublicAPI implements OtherPublicAPI {
    private final HolidaysTable holidaysTable = new DefaultHolidaysTable();

    @Override
    public HolidaysTable getHolidaysTable() {
        return this.holidaysTable;
    }

    @Override
    public KaesTable getKaesTable() {
        return null;
    }
}

package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterTable {
    CounterDto[] getAllCounters();
    void addCounter(CounterDto counter);
    void updateCounter(CounterDto counter);
    void deleteCounter(CounterDto counter);
}

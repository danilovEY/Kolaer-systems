package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.Counter;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterTable {
    Counter[] getAllCounters();
    void addCounter(Counter counter);
    void updateCounter(Counter counter);
    void deleteCounter(Counter counter);
}

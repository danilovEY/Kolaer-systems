package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterTable {
    ServerResponse<List<CounterDto>> getAllCounters();
    ServerResponse addCounter(CounterDto counter);
    ServerResponse updateCounter(CounterDto counter);
    ServerResponse deleteCounter(CounterDto counter);
}

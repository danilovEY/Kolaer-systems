package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

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

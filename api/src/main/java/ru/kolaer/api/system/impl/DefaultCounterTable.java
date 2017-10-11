package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.CounterTable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultCounterTable implements CounterTable {
    @Override
    public ServerResponse<List<CounterDto>> getAllCounters() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse addCounter(CounterDto counter) {
        log.info("Добавлен счетчик: {}", Optional.ofNullable(counter).orElse(new CounterDto()).getTitle());
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse updateCounter(CounterDto counter) {
        log.info("Обновлен счетчик: {}", Optional.ofNullable(counter).orElse(new CounterDto()).getTitle());
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse deleteCounter(CounterDto counter) {
        log.info("Удален счетчик: {}", Optional.ofNullable(counter).orElse(new CounterDto()).getTitle());
        return ServerResponse.createServerResponse();
    }
}

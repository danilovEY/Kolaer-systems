package ru.kolaer.server.counter.service;

import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.counter.model.entity.CounterEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface CounterConverter extends BaseConverter<CounterDto, CounterEntity> {
}

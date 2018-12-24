package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.entity.other.CounterEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface CounterConverter extends BaseConverter<CounterDto, CounterEntity> {
}

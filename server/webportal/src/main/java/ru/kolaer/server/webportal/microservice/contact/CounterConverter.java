package ru.kolaer.server.webportal.microservice.contact;

import ru.kolaer.common.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.webportal.microservice.counter.CounterEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface CounterConverter extends BaseConverter<CounterDto, CounterEntity> {
}

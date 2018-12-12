package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.webportal.model.entity.other.CounterEntity;
import ru.kolaer.server.webportal.model.servirce.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface CounterConverter extends BaseConverter<CounterDto, CounterEntity> {
}

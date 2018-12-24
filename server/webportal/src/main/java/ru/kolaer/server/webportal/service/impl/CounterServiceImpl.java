package ru.kolaer.server.webportal.service.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.webportal.model.converter.CounterConverter;
import ru.kolaer.server.webportal.model.dao.CounterDao;
import ru.kolaer.server.webportal.model.entity.other.CounterEntity;
import ru.kolaer.server.webportal.service.CounterService;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service
public class CounterServiceImpl extends AbstractDefaultService<CounterDto, CounterEntity, CounterDao, CounterConverter> implements CounterService {

    protected CounterServiceImpl(CounterDao counterDao, CounterConverter converter) {
        super(counterDao, converter);
    }
}

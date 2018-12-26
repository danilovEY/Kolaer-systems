package ru.kolaer.server.counter.service;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.counter.CounterDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.counter.dao.CounterDao;
import ru.kolaer.server.counter.model.entity.CounterEntity;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service
public class CounterServiceImpl extends AbstractDefaultService<CounterDto, CounterEntity, CounterDao, CounterConverter> implements CounterService {

    protected CounterServiceImpl(CounterDao counterDao, CounterConverter converter) {
        super(counterDao, converter);
    }
}

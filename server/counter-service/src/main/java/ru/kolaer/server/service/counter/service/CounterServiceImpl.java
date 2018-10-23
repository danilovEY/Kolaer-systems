package ru.kolaer.server.service.counter.service;

import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.service.counter.converter.CounterConverter;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.service.counter.entity.CounterEntity;
import ru.kolaer.server.service.counter.repository.CounterRepository;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service
public class CounterServiceImpl extends AbstractDefaultService<CounterDto, CounterEntity, CounterRepository, CounterConverter> implements CounterService {

    protected CounterServiceImpl(CounterRepository counterDao, CounterConverter converter) {
        super(counterDao, converter);
    }
}

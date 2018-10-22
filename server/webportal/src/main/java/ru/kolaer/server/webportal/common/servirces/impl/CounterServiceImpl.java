package ru.kolaer.server.webportal.common.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.counter.CounterService;
import ru.kolaer.server.webportal.microservice.contact.CounterConverter;
import ru.kolaer.server.webportal.microservice.counter.CounterRepository;
import ru.kolaer.server.webportal.microservice.counter.CounterEntity;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service
public class CounterServiceImpl extends AbstractDefaultService<CounterDto, CounterEntity, CounterRepository, CounterConverter> implements CounterService {

    protected CounterServiceImpl(CounterRepository counterDao, CounterConverter converter) {
        super(counterDao, converter);
    }
}

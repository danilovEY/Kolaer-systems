package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.CounterDto;
import ru.kolaer.server.webportal.mvc.model.converter.CounterConverter;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.CounterEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.CounterService;

/**
 * Created by danilovey on 25.08.2016.
 */
@Service
public class CounterServiceImpl extends AbstractDefaultService<CounterDto, CounterEntity, CounterDao, CounterConverter> implements CounterService {

    protected CounterServiceImpl(CounterDao counterDao, CounterConverter converter) {
        super(counterDao, converter);
    }
}

package ru.kolaer.server.service.counter.repository;

import ru.kolaer.server.service.counter.dto.FindCounterRequest;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.service.counter.entity.CounterEntity;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterRepository extends DefaultRepository<CounterEntity> {
    List<CounterEntity> find(FindCounterRequest request);
}

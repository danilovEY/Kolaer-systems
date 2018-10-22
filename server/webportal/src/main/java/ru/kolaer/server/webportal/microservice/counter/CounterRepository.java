package ru.kolaer.server.webportal.microservice.counter;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterRepository extends DefaultRepository<CounterEntity> {
    List<CounterEntity> find(FindCounterRequest request);
}

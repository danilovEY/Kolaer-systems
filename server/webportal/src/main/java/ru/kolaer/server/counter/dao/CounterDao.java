package ru.kolaer.server.counter.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.core.model.dto.counter.FindCounterRequest;
import ru.kolaer.server.counter.model.entity.CounterEntity;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterDao extends DefaultDao<CounterEntity> {
    List<CounterEntity> find(FindCounterRequest request);
}

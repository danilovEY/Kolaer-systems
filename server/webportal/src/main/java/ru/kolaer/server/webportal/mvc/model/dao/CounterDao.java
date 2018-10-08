package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.dto.counter.FindCounterRequest;
import ru.kolaer.server.webportal.mvc.model.entities.other.CounterEntity;

import java.util.List;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterDao extends DefaultDao<CounterEntity> {
    List<CounterEntity> find(FindCounterRequest request);
}

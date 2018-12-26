package ru.kolaer.server.employee.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.employee.model.entity.TypeWorkEntity;
import ru.kolaer.server.employee.model.request.FindTypeWorkRequest;

import java.util.List;

public interface TypeWorkDao extends DefaultDao<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

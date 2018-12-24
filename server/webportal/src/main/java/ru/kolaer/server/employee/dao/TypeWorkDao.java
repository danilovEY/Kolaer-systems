package ru.kolaer.server.employee.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.dto.typework.FindTypeWorkRequest;
import ru.kolaer.server.webportal.model.entity.typework.TypeWorkEntity;

import java.util.List;

public interface TypeWorkDao extends DefaultDao<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.dto.typework.FindTypeWorkRequest;
import ru.kolaer.server.webportal.mvc.model.entities.typework.TypeWorkEntity;

import java.util.List;

public interface TypeWorkDao extends DefaultDao<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

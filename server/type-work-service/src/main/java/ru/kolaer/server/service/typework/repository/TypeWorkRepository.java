package ru.kolaer.server.service.typework.repository;

import ru.kolaer.server.service.typework.entity.TypeWorkEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.service.typework.dto.FindTypeWorkRequest;

import java.util.List;

public interface TypeWorkRepository extends DefaultRepository<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

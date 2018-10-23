package ru.kolaer.server.webportal.microservice.typework.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.typework.dto.FindTypeWorkRequest;
import ru.kolaer.server.webportal.microservice.typework.entity.TypeWorkEntity;

import java.util.List;

public interface TypeWorkRepository extends DefaultRepository<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

package ru.kolaer.server.webportal.microservice.typework;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

public interface TypeWorkRepository extends DefaultRepository<TypeWorkEntity> {

    long findCountAll(FindTypeWorkRequest request);
    List<TypeWorkEntity> findAll(FindTypeWorkRequest request);
}

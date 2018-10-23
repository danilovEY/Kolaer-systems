package ru.kolaer.server.webportal.microservice.department.repository;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.department.dto.FindDepartmentPageRequest;
import ru.kolaer.server.webportal.microservice.department.entity.DepartmentEntity;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentRepository extends DefaultRepository<DepartmentEntity> {
    DepartmentEntity findByName(String name);

    long findCount(FindDepartmentPageRequest request);

    List<DepartmentEntity> find(FindDepartmentPageRequest request);
}

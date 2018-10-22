package ru.kolaer.server.webportal.microservice.department;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentRepository extends DefaultRepository<DepartmentEntity> {
    DepartmentEntity findByName(String name);

    long findCount(FindDepartmentPageRequest request);

    List<DepartmentEntity> find(FindDepartmentPageRequest request);
}

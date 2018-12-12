package ru.kolaer.server.webportal.model.dao;


import ru.kolaer.server.webportal.model.dto.department.FindDepartmentPageRequest;
import ru.kolaer.server.webportal.model.entity.general.DepartmentEntity;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentDao extends DefaultDao<DepartmentEntity> {
    DepartmentEntity findByName(String name);

    long findCount(FindDepartmentPageRequest request);

    List<DepartmentEntity> find(FindDepartmentPageRequest request);
}

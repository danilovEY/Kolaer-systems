package ru.kolaer.server.employee.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;

import javax.persistence.EntityManagerFactory;

@Deprecated
@Repository
public class DepartmentDao extends AbstractDefaultDao<DepartmentEntity> {

    protected DepartmentDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, DepartmentEntity.class);
    }

}

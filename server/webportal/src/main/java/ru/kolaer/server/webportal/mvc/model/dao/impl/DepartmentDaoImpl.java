package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;

/**
 * Created by danilovey on 12.09.2016.
 */
@Repository
public class DepartmentDaoImpl extends AbstractDefaultDao<DepartmentEntity> implements DepartmentDao {

    protected DepartmentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, DepartmentEntity.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentEntity findByName(String name) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " dep WHERE dep.name = :name", DepartmentEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }
}

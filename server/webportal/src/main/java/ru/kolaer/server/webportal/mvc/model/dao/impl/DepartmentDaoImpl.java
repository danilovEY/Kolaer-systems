package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
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

    @Override
    public DepartmentEntity checkValueBeforePersist(DepartmentEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Подразделение NULL");
        }

        if(StringUtils.isEmpty(entity.getName()) || StringUtils.isEmpty(entity.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В подразделении пустое имя или абревиатура: " + entity.toString(),
                    ErrorCode.PRE_SQL_EXCEPTION);
        }

        return entity;
    }
}

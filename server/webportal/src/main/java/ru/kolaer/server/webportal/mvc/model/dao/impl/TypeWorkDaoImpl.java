package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.TypeWorkDao;
import ru.kolaer.server.webportal.mvc.model.entities.typework.TypeWorkEntity;

@Repository
public class TypeWorkDaoImpl extends AbstractDefaultDao<TypeWorkEntity> implements TypeWorkDao {

    public TypeWorkDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, TypeWorkEntity.class);
    }
}

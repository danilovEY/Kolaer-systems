package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.VacationDao;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;

public class VacationDaoImpl extends AbstractDefaultDao<VacationEntity> implements VacationDao {

    public VacationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, VacationEntity.class);
    }

}

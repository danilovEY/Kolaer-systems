package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntity;

import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 */
@Repository(value = "employeeDao")
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralEmployeesEntity.class).list();
    }

    @Override
    public GeneralEmployeesEntity findByID(short id) {
        return null;
    }

    @Override
    public void save(GeneralEmployeesEntity obj) {

    }
}

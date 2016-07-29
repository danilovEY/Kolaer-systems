package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

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
    public List<GeneralEmployeesEntityDecorator> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralEmployeesEntityDecorator.class).list();
    }

    @Override
    public GeneralEmployeesEntityDecorator findByID(int id) {
        return null;
    }

    @Override
    public void save(GeneralEmployeesEntityDecorator obj) {

    }
}

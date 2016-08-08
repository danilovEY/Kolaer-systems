package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
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
    public List<GeneralEmployeesEntity> findAll() {
        final List<GeneralEmployeesEntity> list = sessionFactory.getCurrentSession().createCriteria(GeneralEmployeesEntityDecorator.class).list();
        return list;
    }

    @Override
    public GeneralEmployeesEntity findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(GeneralEmployeesEntityDecorator.class, id);
    }

    @Override
    public void persist(GeneralEmployeesEntity obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }

}

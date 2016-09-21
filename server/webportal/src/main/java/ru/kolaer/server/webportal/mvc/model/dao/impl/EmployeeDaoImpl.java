package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

import java.util.Collections;
import java.util.Date;
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
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp").list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralEmployeesEntity findByID(int id) {
        final GeneralEmployeesEntity result = (GeneralEmployeesEntity) this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.pnumber = :id")
                .setParameter("id", id).uniqueResult();
        result.getDepartament().getAbbreviatedName();
        result.getDepartament().getName();
        result.getDepartament().getId();
        return result;
    }

    @Override
    @Transactional
    public void persist(GeneralEmployeesEntity obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(GeneralEmployeesEntity obj) {

    }

    @Override
    @Transactional
    public void update(GeneralEmployeesEntity entity) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> findEmployeeByInitials(String initials) {
        final List<GeneralEmployeesEntity> result = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.initials LIKE :initials")
                .setParameter("initials", "%" + initials + "%").list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUserRangeBirthday(final Date startDate, final Date endDate) {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where t.birthday BETWEEN :startDate AND :endDate")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUsersByBirthday(final Date date) {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
                .setParameter("date", date)
                .list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUserBirthdayToday() {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)")
                .list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountUserBirthday(final Date date) {
        final Number result = (Number) sessionFactory.getCurrentSession().createQuery("SELECT count(t) FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
                .setParameter("date", date)
                .list();
        return result.intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUsersByInitials(final String initials) {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator AS t JOIN FETCH t.departament where t.initials like :initials")
                .setParameter("initials", "%" + initials + "%")
                .list();
        return result;
    }
}

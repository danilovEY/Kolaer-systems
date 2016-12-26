package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import javax.validation.constraints.NotNull;
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
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp ORDER BY emp.initials").list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralEmployeesEntity findByID(Integer id) {
        final GeneralEmployeesEntity result = (GeneralEmployeesEntity) this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.pnumber = :id ORDER BY emp.initials")
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
    public void delete(List<GeneralEmployeesEntity> objs) {

    }

    @Override
    @Transactional
    public void update(GeneralEmployeesEntity entity) {

    }

    @Override
    public void update(List<GeneralEmployeesEntity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    public List<GeneralEmployeesEntity> findEmployeeByInitials(String initials) {
        final List<GeneralEmployeesEntity> result = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.initials LIKE :initials ORDER BY emp.initials")
                .setParameter("initials", "%" + initials + "%").list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> findByDepartamentById(Integer id) {
        final List<GeneralEmployeesEntity> result = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id ORDER BY emp.initials")
                .setParameter("id", id).list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });
        return result;
    }

    @Transactional(readOnly = true)
    public Page<GeneralEmployeesEntity> findByDepartamentById(int page, int pageSize, Integer id) {
        Session currentSession = this.sessionFactory.getCurrentSession();
        final Long count = (Long) currentSession.createQuery("SELECT COUNT(emp.pnumber) FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id")
                .setParameter("id", id)
                .uniqueResult();

        final List<GeneralEmployeesEntity> result = this.sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator emp WHERE emp.departament.id = :id ORDER BY emp.initials")
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
        result.forEach(emp -> {
            emp.getDepartament().getAbbreviatedName();
            emp.getDepartament().getName();
            emp.getDepartament().getId();
        });

        return new Page<>(result, page, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUserRangeBirthday(final Date startDate, final Date endDate) {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where t.birthday BETWEEN :startDate AND :endDate ORDER BY t.initials")
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
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) ORDER BY t.initials")
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
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) ORDER BY t.initials")
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
        final Long result = (Long) sessionFactory.getCurrentSession().createQuery("SELECT count(t.pnumber) FROM GeneralEmployeesEntityDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
                .setParameter("date", date)
                .uniqueResult();
        return result.intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralEmployeesEntity> getUsersByInitials(final String initials) {
        final List<GeneralEmployeesEntity> result = sessionFactory.getCurrentSession().createQuery("FROM GeneralEmployeesEntityDecorator AS t JOIN FETCH t.departament where t.initials like :initials ORDER BY t.initials")
                .setParameter("initials", "%" + initials + "%")
                .list();
        return result;
    }
}

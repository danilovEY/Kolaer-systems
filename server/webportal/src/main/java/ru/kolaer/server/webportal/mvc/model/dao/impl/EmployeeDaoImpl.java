package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 *
 */
@Repository
@Slf4j
public class EmployeeDaoImpl extends AbstractDefaultDao<EmployeeEntity> implements EmployeeDao {

    protected EmployeeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, EmployeeEntity.class);
    }

    @Override
    public List<EmployeeEntity> findEmployeeByInitials(@NonNull String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.initials LIKE :initials ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(@NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(int page, int pageSize, @NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id ORDER BY emp.initials",
                        EmployeeEntity.class)
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountByDepartmentById(Long id) {
        return getSession().createQuery("SELECT COUNT(emp.personnelNumber) FROM " + getEntityName() +
                " emp WHERE emp.department.id = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public EmployeeEntity findByPersonnelNumber(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.personnelNumber = :id", EmployeeEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<EmployeeEntity> getUserRangeBirthday(@NonNull final Date startDate, @NonNull final Date endDate) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where t.birthday BETWEEN :startDate AND :endDate " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUsersByBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("date", date)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUserBirthdayToday() {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .list();
    }

    @Override
    public int getCountUserBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("SELECT count(t.personnelNumber) FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Long.class)
                .setParameter("date", date)
                .uniqueResult()
                .intValue();
    }

    @Override
    public List<EmployeeEntity> getUsersByInitials(@NonNull final String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                " t where t.initials like :initials ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }
}

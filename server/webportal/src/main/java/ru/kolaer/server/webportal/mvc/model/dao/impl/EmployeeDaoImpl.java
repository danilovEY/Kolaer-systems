package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.math.BigInteger;
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
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.initials LIKE :initials AND emp.dismissalDate IS NULL ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(@NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id AND emp.dismissalDate IS NULL ORDER BY emp.initials", EmployeeEntity.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<EmployeeEntity> findByDepartmentById(int page, int pageSize, @NonNull Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.department.id = :id AND emp.dismissalDate IS NULL ORDER BY emp.initials",
                        EmployeeEntity.class)
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountByDepartmentById(Long id) {
        return getSession().createQuery("SELECT COUNT(emp.personnelNumber) FROM " + getEntityName() +
                " emp WHERE emp.department.id = :id AND emp.dismissalDate IS NULL", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public EmployeeEntity findByPersonnelNumber(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " emp WHERE emp.personnelNumber = :id AND emp.dismissalDate IS NULL", EmployeeEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<EmployeeEntity> findEmployeesForContacts(int page, int pageSize, String searchText) {
        return getSession()
                .createNativeQuery("SELECT * FROM employee t " +
                        " LEFT JOIN contact c ON c.id = t.contact_id" +
                        " LEFT JOIN placement pl ON pl.id = c.place_id" +
                        " LEFT JOIN department d ON d.id = t.department_id" +
                        " LEFT JOIN post p ON p.id = t.post_id" +
                        " where t.initials LIKE :searchText OR " +
                        "d.name LIKE :searchText OR " +
                        "d.abbreviated_name LIKE :searchText OR " +
                        "p.abbreviated_name LIKE :searchText OR " +
                        "t.contact_id IS NOT NULL AND (c.email LIKE :searchText OR " +
                        "c.work_phone_number LIKE :searchText OR " +
                        "c.pager LIKE :searchText OR (c.place_id IS NOT NULL AND pl.name LIKE :searchText))",
                        getEntityClass())
                .setParameter("searchText", "%" + searchText + "%")
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public long findCountEmployeesForContacts(String searchText) {
        Object result = getSession()
                .createNativeQuery("SELECT COUNT(t.id) FROM employee t " +
                        " LEFT JOIN contact c ON c.id = t.contact_id" +
                        " LEFT JOIN placement pl ON pl.id = c.place_id" +
                        " LEFT JOIN department d ON d.id = t.department_id" +
                        " LEFT JOIN post p ON p.id = t.post_id" +
                        " where t.initials LIKE :searchText OR " +
                        "d.name LIKE :searchText OR " +
                        "d.abbreviated_name LIKE :searchText OR " +
                        "p.abbreviated_name LIKE :searchText OR " +
                        "t.contact_id IS NOT NULL AND (c.email LIKE :searchText OR " +
                        "c.work_phone_number LIKE :searchText OR " +
                        "c.pager LIKE :searchText OR (c.place_id IS NOT NULL AND pl.name LIKE :searchText))")
                .setParameter("searchText", "%" + searchText + "%")
                .getSingleResult();

        return ((BigInteger) result).longValue();

        //                .createQuery("SELECT COUNT(t.id) FROM " + getEntityName() + " t " +
//                                " where t.initials LIKE :searchText OR " +
//                                "t.department.name LIKE :searchText OR " +
//                                "t.department.abbreviatedName LIKE :searchText OR " +
//                                "t.post.abbreviatedName LIKE :searchText OR " +
//                                "t.contact IS NOT NULL AND (t.contact.email LIKE :searchText OR " +
//                                "t.contact.workPhoneNumber LIKE :searchText OR " +
//                                "t.contact.pager LIKE :searchText OR " +
//                                "t.contact.placement.name LIKE :searchText)",
    }

    @Override
    public List<EmployeeEntity> getUserRangeBirthday(@NonNull final Date startDate, @NonNull final Date endDate) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where t.birthday BETWEEN :startDate AND :endDate AND t.dismissalDate IS NULL " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUsersByBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) AND t.dismissalDate IS NULL " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("date", date)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUserBirthdayToday() {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) AND t.dismissalDate IS NULL " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .list();
    }

    @Override
    public int getCountUserBirthday(@NonNull final Date date) {
        return getSession()
                .createQuery("SELECT count(t.personnelNumber) FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) AND t.dismissalDate IS NULL", Long.class)
                .setParameter("date", date)
                .uniqueResult()
                .intValue();
    }

    @Override
    public List<EmployeeEntity> getUsersByInitials(@NonNull final String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                " t where t.initials like :initials AND t.dismissalDate IS NULL ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("initials", "%" + initials + "%")
                .list();
    }

    @Override
    public EmployeeEntity checkValueBeforePersist(EmployeeEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Сотрудник NULL");
        }

        if(StringUtils.isEmpty(entity.getInitials()) || StringUtils.isEmpty(entity.getFirstName())
                || StringUtils.isEmpty(entity.getSecondName()) || StringUtils.isEmpty(entity.getThirdName())
                || entity.getPersonnelNumber() == null || entity.getGender() == null
                || entity.getDepartmentId() == null || entity.getPostId() == null) {
            throw new UnexpectedRequestParams("У сотрудника пустое Ф.И.О, табельный, пол, подразделение или должность: "
                    + entity.toString(), ErrorCode.PRE_SQL_EXCEPTION);
        }

        return entity;
    }
}

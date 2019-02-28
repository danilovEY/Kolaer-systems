package ru.kolaer.server.employee.dao;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.employee.model.dto.CountEmployeeInDepartmentDto;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.request.EmployeeSortType;
import ru.kolaer.server.employee.model.request.FindEmployeeByDepartment;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danilov on 27.07.2016.
 *
 */
@Repository
@Slf4j
public class EmployeeDaoImpl extends AbstractDefaultDao<EmployeeEntity> implements EmployeeDao {

    @Autowired
    public EmployeeDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EmployeeEntity.class);
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

    /*@Override
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
                        "c.pager LIKE :searchText OR (c.place_id IS NOT NULL AND pl.name LIKE :searchText)) ORDER BY t.initials",
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
    }

    @Override
    public List<EmployeeEntity> findEmployeeByDepIdAndContactType(int page, int pageSize, long depId, ContactType type) {
        return getSession()
                .createNativeQuery("SELECT * FROM employee t " +
                                "LEFT JOIN contact c ON c.id = t.contact_id " +
                                "where t.department_id = :depId AND " +
                                "((:contactType = :otherContactType AND t.contact_id IS NULL OR t.contact_id IS NOT NULL AND c.type = :contactType) OR " +
                                "t.contact_id IS NOT NULL AND c.type = :contactType) ORDER BY t.initials",
                        getEntityClass())
                .setParameter("depId", depId)
                .setParameter("contactType", type.ordinal())
                .setParameter("otherContactType", ContactType.OTHER.ordinal())
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountEmployeeByDepIdAndContactType(long depId, ContactType type) {
        Object result = getSession()
                .createNativeQuery("SELECT COUNT(t.id) FROM employee t " +
                                "LEFT JOIN contact c ON c.id = t.contact_id " +
                                "where t.department_id = :depId AND " +
                                "((:contactType = :otherContactType AND t.contact_id IS NULL OR t.contact_id IS NOT NULL AND c.type = :contactType) OR " +
                                "t.contact_id IS NOT NULL AND c.type = :contactType)")
                .setParameter("depId", depId)
                .setParameter("contactType", type.ordinal())
                .setParameter("otherContactType", ContactType.OTHER.ordinal())
                .getSingleResult();

        return ((BigInteger) result).longValue();
    }*/

    @Override
    public List<CountEmployeeInDepartmentDto> findEmployeeByDepartmentCount(FindEmployeeByDepartment request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT new ru.kolaer.server.webportal.mvc.model.dto.employee.CountEmployeeInDepartmentDto(" +
                        "emp.departmentId, emp.department.abbreviatedName, COUNT(emp.id)) FROM ")
                .append(getEntityName())
                .append(" AS emp");

        query = query.append(" WHERE emp.dismissalDate IS NULL");

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND emp.departmentId IN (:depIds)");
            params.put("depIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getEmployeeIds())) {
            query = query.append(" AND emp.id IN (:employeeIds)");
            params.put("employeeIds", request.getEmployeeIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            query = query.append(" AND emp.postId IN (:postIds)");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            query = query.append(" AND emp.typeWorkId IN (:typeWorkIds)");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        query = query.append(" GROUP BY emp.departmentId");

        return getSession().createQuery(query.toString(), CountEmployeeInDepartmentDto.class)
                .setProperties(params)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public long findAllEmployeeCount(FindEmployeePageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(emp.id) FROM ")
                .append(getEntityName())
                .append(" AS emp");

        query = query.append(" WHERE emp.dismissalDate IS NULL");

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND emp.departmentId IN (:depIds)");
            params.put("depIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getEmployeeIds())) {
            query = query.append(" AND emp.id IN (:employeeIds)");
            params.put("employeeIds", request.getEmployeeIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            query = query.append(" AND emp.postId IN (:postIds)");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            query = query.append(" AND emp.typeWorkId IN (:typeWorkIds)");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND (LOWER(emp.initials) LIKE :query OR emp.personnelNumber LIKE :query)");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        return getSession().createQuery(query.toString(), Long.class)
                .setProperties(params)
                .uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEntity> findAllEmployee(FindEmployeePageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("FROM ")
                .append(getEntityName())
                .append(" AS emp");

        query = query.append(" WHERE emp.dismissalDate IS NULL");

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND emp.departmentId IN (:depIds)");
            params.put("depIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getEmployeeIds())) {
            query = query.append(" AND emp.id IN (:employeeIds)");
            params.put("employeeIds", request.getEmployeeIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            query = query.append(" AND emp.postId IN (:postIds)");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            query = query.append(" AND emp.typeWorkId IN (:typeWorkIds)");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND (LOWER(emp.initials) LIKE :query OR emp.personnelNumber LIKE :query)");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        Query<EmployeeEntity> entityQuery = getSession()
                .createQuery(query.append(getOrder(request.getSort())).toString(), EmployeeEntity.class);

        if(!request.isOnOnePage()) {
            entityQuery = entityQuery
                    .setMaxResults(request.getPageSize())
                    .setFirstResult(getFirstResult(request));
        }

        return entityQuery
                .setProperties(params)
                .list();
    }

    private String getOrder(EmployeeSortType sortType) {
        if (sortType == null) return " ORDER BY emp.id ASC";

        switch (sortType) {
            case INITIALS_ASC: return " ORDER BY emp.initials ASC";
            case ID_ASC:
                default: return " ORDER BY emp.id ASC";
        }
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

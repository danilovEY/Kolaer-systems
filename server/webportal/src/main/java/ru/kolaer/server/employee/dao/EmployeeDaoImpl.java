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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public Set<Long> findByDepartmentById(@NonNull Long id) {
        return getSession()
                .createQuery("SELECT id FROM " + getEntityName() + " emp WHERE emp.department.id = :id AND emp.dismissalDate IS NULL ORDER BY emp.initials", Long.class)
                .setParameter("id", id)
                .list()
                .stream()
                .collect(Collectors.toSet());
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

        query = query.append(" WHERE emp.id > 0");

        if (request.getFindByDeleted() != null) {
            query = request.getFindByDeleted().equals(Boolean.TRUE)
                    ? query.append(" AND emp.dismissalDate IS NOT NULL")
                    : query.append(" AND emp.dismissalDate IS NULL");
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            query = query.append(" AND emp.id IN (:ids)");
            params.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND emp.departmentId IN (:depIds)");
            params.put("depIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            query = query.append(" AND emp.postId IN (:postIds)");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            query = query.append(" AND emp.typeWorkId IN (:typeWorkIds)");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        if (StringUtils.hasText(request.getFindByInitials())) {
            query = query.append(" AND emp.initials LIKE :findByInitials");
            params.put("findByInitials", "%" + request.getFindByInitials().trim().toLowerCase() + "%");
        }

        if (request.getFindByPersonnelNumber() != null) {
            query = query.append(" AND str(emp.personnelNumber) LIKE :findByPersonnelNumber");
            params.put("findByPersonnelNumber", "%" + request.getFindByPersonnelNumber() + "%");
        }

        if (StringUtils.hasText(request.getFindByAll())) {
            query = query.append(" AND (emp.initials LIKE :findByAll OR str(emp.personnelNumber) LIKE :findByAll)");
            params.put("findByAll", "%" + request.getFindByAll() + "%");
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

        query = query.append(" WHERE emp.id > 0");

        if (request.getFindByDeleted() != null) {
            query = request.getFindByDeleted().equals(Boolean.TRUE)
                    ? query.append(" AND emp.dismissalDate IS NOT NULL")
                    : query.append(" AND emp.dismissalDate IS NULL");
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            query = query.append(" AND emp.id IN (:ids)");
            params.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND emp.departmentId IN (:depIds)");
            params.put("depIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            query = query.append(" AND emp.postId IN (:postIds)");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            query = query.append(" AND emp.typeWorkId IN (:typeWorkIds)");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        if (StringUtils.hasText(request.getFindByInitials())) {
            query = query.append(" AND emp.initials LIKE :findByInitials");
            params.put("findByInitials", "%" + request.getFindByInitials().trim().toLowerCase() + "%");
        }

        if (request.getFindByPersonnelNumber() != null) {
            query = query.append(" AND str(emp.personnelNumber) LIKE :findByPersonnelNumber");
            params.put("findByPersonnelNumber", "%" + request.getFindByPersonnelNumber() + "%");
        }

        if (StringUtils.hasText(request.getFindByAll())) {
            query = query.append(" AND (emp.initials LIKE :findByAll OR str(emp.personnelNumber) LIKE :findByAll)");
            params.put("findByAll", "%" + request.getFindByAll() + "%");
        }

        Query<EmployeeEntity> entityQuery = getSession()
                .createQuery(query.append(getOrder(request.getSorts())).toString(), EmployeeEntity.class);

        return entityQuery
                .setMaxResults(request.getPageSize())
                .setFirstResult(getFirstResult(request))
                .setProperties(params)
                .list();
    }

    @Override
    public Optional<Long> employeeEqualDepartment(long currentEmployeeId, long checkedEmployeeId) {
        return getSession()
                .createQuery("SELECT e1.departmentId FROM " + getEntityName() + " AS e1 WHERE e1.id=:checkedEmployeeId AND e1.departmentId=(SELECT e2.departmentId FROM " + getEntityName() + " AS e2 WHERE e2.id=:currentEmployeeId)", Long.class)
                .setParameter("checkedEmployeeId", checkedEmployeeId)
                .setParameter("currentEmployeeId", currentEmployeeId)
                .uniqueResultOptional();
    }

    private String getOrder(EmployeeSortType sort) {
        switch (sort) {
            case INITIALS_ASC: return "emp.initials ASC";
            case INITIALS_DESC: return "emp.initials DESC";
            case PERSONNEL_NUMBER_ASC: return "emp.personnelNumber ASC";
            case PERSONNEL_NUMBER_DESC: return "emp.personnelNumber DESC";
            case ID_ASC:
            default: return " emp.id ASC";
        }
    }

    private String getOrder(Collection<EmployeeSortType> sorts) {
        if (CollectionUtils.isEmpty(sorts)) return " ORDER BY emp.id ASC";


        return " ORDER BY " + sorts
                .stream()
                .map(this::getOrder)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<EmployeeEntity> getUserRangeBirthday(@NonNull final LocalDate startDate, @NonNull final LocalDate endDate) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where t.birthday BETWEEN :startDate AND :endDate AND t.dismissalDate IS NULL " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

    @Override
    public List<EmployeeEntity> getUsersByBirthday(@NonNull final LocalDate date) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) AND t.dismissalDate IS NULL " +
                        "ORDER BY t.initials", EmployeeEntity.class)
                .setParameter("date", date.toString())
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
    public int getCountUserBirthday(@NonNull final LocalDate date) {
        return getSession()
                .createQuery("SELECT count(t.personnelNumber) FROM " + getEntityName() +
                        " t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date) AND t.dismissalDate IS NULL", Long.class)
                .setParameter("date", date.toString())
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

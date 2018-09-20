package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.VacationDao;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VacationDaoImpl extends AbstractDefaultDao<VacationEntity> implements VacationDao {

    public VacationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, VacationEntity.class);
    }

    @Override
    public long findCountVacation(FindVacationPageRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeId", request.getEmployeeId());
        params.put("year", request.getYear());

        StringBuilder sqlQuery = new StringBuilder()
                .append("SELECT COUNT(id) FROM ")
                .append(getEntityName())
                .append(" WHERE employeeId = :employeeId AND YEAR(vacationFrom) = :year");

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), Long.class)
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<VacationEntity> findAllVacation(FindVacationPageRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeId", request.getEmployeeId());
        params.put("year", request.getYear());

        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName())
                .append(" WHERE employeeId = :employeeId AND YEAR(vacationFrom) = :year");

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), getEntityClass())
                .setProperties(params)
                .setMaxResults(request.getPageSize())
                .setFirstResult(getFirstResult(request))
                .list();
    }

    @Override
    public VacationBalanceEntity findBalance(FindBalanceRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("employeeId", request.getEmployeeId());

        String sqlQuery = "FROM " +
                getEntityName(VacationBalanceEntity.class) +
                " WHERE employeeId = :employeeId";

        return getSession()
                .createQuery(sqlQuery, VacationBalanceEntity.class)
                .setProperties(param)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public Long findCountPeriods(FindVacationPeriodPageRequest request) {
        String sqlQuery = "SELECT COUNT(id) FROM " +
                getEntityName(VacationPeriodEntity.class);

        return getSession()
                .createQuery(sqlQuery, Long.class)
                .setMaxResults(request.getPageSize())
                .setFirstResult(getFirstResult(request))
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<VacationPeriodEntity> findAllPeriods(FindVacationPeriodPageRequest request) {
        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName(VacationPeriodEntity.class));

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), VacationPeriodEntity.class)
                .setMaxResults(request.getPageSize())
                .setFirstResult(getFirstResult(request))
                .list();
    }

    @Override
    public VacationPeriodEntity findLastPeriods(FindVacationPeriodPageRequest request) {
        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName(VacationPeriodEntity.class));

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), VacationPeriodEntity.class)
                .setMaxResults(1)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public VacationPeriodEntity findPeriodsByYear(int year) {
        return getSession()
                .createQuery("FROM " + getEntityName(VacationPeriodEntity.class) + " WHERE year = :year", VacationPeriodEntity.class)
                .setParameter("year", year)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public VacationBalanceEntity save(VacationBalanceEntity vacationBalanceEntity) {
        if (vacationBalanceEntity.getId() == null) {
            getSession().persist(vacationBalanceEntity);
        } else {
            getSession().update(vacationBalanceEntity);
        }

        return vacationBalanceEntity;
    }

    @Override
    public List<VacationEntity> findAll(GenerateReportCalendarRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName());

        if (!request.isAllDepartment() && !request.getDepartmentIds().isEmpty()) {
            sqlQuery = sqlQuery.append(" WHERE employee.departmentId IN (:departmentIds) AND ");
            params.put("departmentIds", request.getDepartmentIds());
        } else {
            sqlQuery = sqlQuery.append(" WHERE ");
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            sqlQuery = sqlQuery.append(" employee.postId IN (:postIds) AND ");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            sqlQuery = sqlQuery.append(" employee.typeWorkId IN (:typeWorkIds) AND ");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        sqlQuery = sqlQuery.append("((vacationFrom <= :vacationFrom AND vacationTo >= :vacationFrom OR " +
                "vacationFrom <= :vacationTo AND vacationTo >= :vacationTo) OR " +
                "(vacationFrom >= :vacationFrom AND vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        sqlQuery = sqlQuery.append("ORDER BY vacationFrom ASC, vacationTo ASC");

        return getSession()
                .createQuery(sqlQuery.toString(), getEntityClass())
                .setProperties(params)
                .list();
    }

    @Override
    public VacationTotalCountEntity findAllVacationTotalCount(GenerateReportTotalCountRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("SELECT new ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationTotalCountEntity(" +
                        "COUNT(DISTINCT v.employeeId), (SELECT COUNT(ee.id) FROM EmployeeEntity AS ee)) ")
                .append("FROM ")
                .append(getEntityName())
                .append(" AS v LEFT JOIN VacationBalanceEntity AS vb ON v.employeeId = vb.employeeId");

        int currentYear = LocalDate.now().getYear();
        int yearFrom = request.getFrom().getYear();

        if (currentYear < yearFrom) {
            sqlQuery = sqlQuery.append(" WHERE vb.nextYearBalance <= 0 AND");
        } else if (currentYear == yearFrom) {
            sqlQuery = sqlQuery.append(" WHERE vb.currentYearBalance <= 0 AND");
        } else {
            sqlQuery = sqlQuery.append(" WHERE vb.prevYearBalance <= 0 AND");
        }

        sqlQuery = sqlQuery.append("((v.vacationFrom <= :vacationFrom AND v.vacationTo >= :vacationFrom OR " +
                "v.vacationFrom <= :vacationTo AND v.vacationTo >= :vacationTo) OR " +
                "(v.vacationFrom >= :vacationFrom AND v.vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        return getSession()
                .createQuery(sqlQuery.toString(), VacationTotalCountEntity.class)
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public List<VacationTotalCountDepartmentEntity> findVacationTotalCountDepartment(GenerateReportTotalCountRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("SELECT new ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationTotalCountDepartmentEntity(" +
                        "v.employee.departmentId, COUNT(DISTINCT v.employeeId), (SELECT COUNT(ee.id) FROM EmployeeEntity AS ee WHERE ee.departmentId = v.employee.departmentId)) ")
                .append("FROM ")
                .append(getEntityName())
                .append(" AS v LEFT JOIN VacationBalanceEntity AS vb ON v.employeeId = vb.employeeId");

        int currentYear = LocalDate.now().getYear();
        int yearFrom = request.getFrom().getYear();

        if (currentYear < yearFrom) {
            sqlQuery = sqlQuery.append(" WHERE vb.nextYearBalance <= 0 AND");
        } else if (currentYear == yearFrom) {
            sqlQuery = sqlQuery.append(" WHERE vb.currentYearBalance <= 0 AND");
        } else {
            sqlQuery = sqlQuery.append(" WHERE vb.prevYearBalance <= 0 AND");
        }

        if (!request.getDepartmentIds().isEmpty()) {
            sqlQuery = sqlQuery.append(" v.employee.departmentId IN (:departmentIds) AND ");
            params.put("departmentIds", request.getDepartmentIds());
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            sqlQuery = sqlQuery.append(" v.employee.postId IN (:postIds) AND ");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            sqlQuery = sqlQuery.append(" v.employee.typeWorkId IN (:typeWorkIds) AND ");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        sqlQuery = sqlQuery.append("((v.vacationFrom <= :vacationFrom AND v.vacationTo >= :vacationFrom OR " +
                "v.vacationFrom <= :vacationTo AND v.vacationTo >= :vacationTo) OR " +
                "(v.vacationFrom >= :vacationFrom AND v.vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        sqlQuery = sqlQuery.append("GROUP BY v.employee.departmentId");

        return getSession()
                .createQuery(sqlQuery.toString(), VacationTotalCountDepartmentEntity.class)
                .setProperties(params)
                .list();
    }

    @Override
    public List<VacationEntity> findAll(GenerateReportDistributeRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName());

        if (!request.getDepartmentIds().isEmpty()) {
            sqlQuery = sqlQuery.append(" WHERE employee.departmentId IN (:departmentIds) AND ");
            params.put("departmentIds", request.getDepartmentIds());
        } else {
            sqlQuery = sqlQuery.append(" WHERE ");
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            sqlQuery = sqlQuery.append(" employee.postId IN (:postIds) AND ");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            sqlQuery = sqlQuery.append(" employee.typeWorkId IN (:typeWorkIds) AND ");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        sqlQuery = sqlQuery.append("((vacationFrom <= :vacationFrom AND vacationTo >= :vacationFrom OR " +
                "vacationFrom <= :vacationTo AND vacationTo >= :vacationTo) OR " +
                "(vacationFrom >= :vacationFrom AND vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        sqlQuery = sqlQuery.append("ORDER BY vacationFrom ASC, vacationTo ASC");

        return getSession()
                .createQuery(sqlQuery.toString(), getEntityClass())
                .setProperties(params)
                .list();
    }

    @Override
    public long findCountVacation(FindVacationByDepartmentRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("SELECT COUNT(id) FROM ")
                .append(getEntityName());

        if (!request.getDepartmentIds().isEmpty()) {
            sqlQuery = sqlQuery.append(" WHERE employee.departmentId IN (:departmentIds) AND ");
            params.put("departmentIds", request.getDepartmentIds());
        } else {
            sqlQuery = sqlQuery.append(" WHERE ");
        }

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            sqlQuery = sqlQuery.append(" employee.postId IN (:postIds) AND ");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            sqlQuery = sqlQuery.append(" employee.typeWorkId IN (:typeWorkIds) AND ");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        sqlQuery = sqlQuery.append("((vacationFrom <= :vacationFrom AND vacationTo >= :vacationFrom OR " +
                "vacationFrom <= :vacationTo AND vacationTo >= :vacationTo) OR " +
                "(vacationFrom >= :vacationFrom AND vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        return getSession()
                .createQuery(sqlQuery.toString(), Long.class)
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<VacationEntity> findAll(GenerateReportExportRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder()
                .append("FROM ")
                .append(getEntityName());



        sqlQuery = sqlQuery.append(" WHERE employee.departmentId = :departmentId AND ");
        params.put("departmentId", request.getDepartmentId());

        if (!CollectionUtils.isEmpty(request.getPostIds())) {
            sqlQuery = sqlQuery.append(" employee.postId IN (:postIds) AND ");
            params.put("postIds", request.getPostIds());
        }

        if (!CollectionUtils.isEmpty(request.getTypeWorkIds())) {
            sqlQuery = sqlQuery.append(" employee.typeWorkId IN (:typeWorkIds) AND ");
            params.put("typeWorkIds", request.getTypeWorkIds());
        }

        sqlQuery = sqlQuery.append("((vacationFrom <= :vacationFrom AND vacationTo >= :vacationFrom OR " +
                "vacationFrom <= :vacationTo AND vacationTo >= :vacationTo) OR " +
                "(vacationFrom >= :vacationFrom AND vacationTo <= :vacationTo)) ");
        params.put("vacationFrom", request.getFrom());
        params.put("vacationTo", request.getTo());

        sqlQuery = sqlQuery.append("ORDER BY vacationFrom ASC, vacationTo ASC");

        return getSession()
                .createQuery(sqlQuery.toString(), getEntityClass())
                .setProperties(params)
                .list();
    }

    private String getOrder(VacationPeriodSortType type) {
        if (type == null) return " ORDER BY year DESC";

        switch (type) {
            case YEAR_DESC:
                default: return " ORDER BY year DESC";
        }
    }

    private String getOrder(VacationSortType type) {
        if (type == null) return " ORDER BY vacationFrom DESC";

        switch (type) {
            case DATE_FROM_DESC:
                default: return " ORDER BY vacationFrom DESC";
        }
    }

}

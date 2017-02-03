package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Доа для работы с сотрудниками.
 */
public interface EmployeeDao extends DefaultDao<EmployeeEntity>, BirthdayDao<EmployeeEntity>, UpdateEmployeesDao {
    List<EmployeeEntity> findEmployeeByInitials(String initials);

    List<EmployeeEntity> findByDepartmentById(Integer id);

    Page<EmployeeEntity> findByDepartmentById(int page, int pageSize, Integer id);

    EmployeeEntity findByPersonnelNumber(Integer id);
}

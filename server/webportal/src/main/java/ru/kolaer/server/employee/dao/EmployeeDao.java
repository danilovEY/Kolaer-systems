package ru.kolaer.server.employee.dao;


import ru.kolaer.server.core.dao.BirthdayDao;
import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.employee.model.dto.CountEmployeeInDepartmentDto;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.request.FindEmployeeByDepartment;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Danilov on 27.07.2016.
 * Доа для работы с сотрудниками.
 */
public interface EmployeeDao extends DefaultDao<EmployeeEntity>, BirthdayDao<EmployeeEntity> {
    List<EmployeeEntity> findEmployeeByInitials(String initials);

    Set<Long> findByDepartmentById(Long id);

    List<EmployeeEntity> findByDepartmentById(int page, int pageSize, Long id);
    Long findCountByDepartmentById(Long id);

    EmployeeEntity findByPersonnelNumber(Long id);

//    List<EmployeeEntity> findEmployeesForContacts(int page, int pageSize, String searchText);
//    long findCountEmployeesForContacts(String searchText);

//    List<EmployeeEntity> findEmployeeByDepIdAndContactType(int page, int pageSize, long depId, ContactType type);
//    Long findCountEmployeeByDepIdAndContactType(long depId, ContactType type);

    List<CountEmployeeInDepartmentDto> findEmployeeByDepartmentCount(FindEmployeeByDepartment request);

    long findAllEmployeeCount(FindEmployeePageRequest request);
    List<EmployeeEntity> findAllEmployee(FindEmployeePageRequest request);

    Optional<Long> employeeEqualDepartment(long currentEmployeeId, long checkedEmployeeId);
}

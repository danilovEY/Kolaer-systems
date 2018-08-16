package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.dto.employee.FindEmployeePageRequest;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactType;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Доа для работы с сотрудниками.
 */
public interface EmployeeDao extends DefaultDao<EmployeeEntity>, BirthdayDao<EmployeeEntity> {
    List<EmployeeEntity> findEmployeeByInitials(String initials);

    List<EmployeeEntity> findByDepartmentById(Long id);

    List<EmployeeEntity> findByDepartmentById(int page, int pageSize, Long id);
    Long findCountByDepartmentById(Long id);

    EmployeeEntity findByPersonnelNumber(Long id);

    List<EmployeeEntity> findEmployeesForContacts(int page, int pageSize, String searchText);

    long findCountEmployeesForContacts(String searchText);

    List<EmployeeEntity> findEmployeeByDepIdAndContactType(int page, int pageSize, long depId, ContactType type);
    Long findCountEmployeeByDepIdAndContactType(long depId, ContactType type);

    long findAllEmployeeCount(FindEmployeePageRequest request);
    List<EmployeeEntity> findAllEmployee(FindEmployeePageRequest request);
}

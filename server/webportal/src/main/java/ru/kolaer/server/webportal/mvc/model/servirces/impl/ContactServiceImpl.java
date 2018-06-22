package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.converter.ContactConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ContactDao;
import ru.kolaer.server.webportal.mvc.model.dto.*;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.PhoneBookService;

import java.util.List;

@Service
public class PhoneBookServiceImpl implements PhoneBookService {
    private final ContactDao contactDao;
    private final EmployeeService employeeService;
    private final ContactConverter contactConverter;
    private final DepartmentService departmentService;

    public PhoneBookServiceImpl(ContactDao contactDao,
                                EmployeeService employeeService,
                                ContactConverter contactConverter,
                                DepartmentService departmentService) {
        this.contactDao = contactDao;
        this.employeeService = employeeService;
        this.contactConverter = contactConverter;
        this.departmentService = departmentService;
    }

    List<DepartmentDto> getAllDepartments() {
        DepartmentSort departmentSort = new DepartmentSort();
        departmentSort.setSortCode(SortType.ASC);

        DepartmentFilter departmentFilter = new DepartmentFilter();
        departmentFilter.setFilterDeleted(false);
        departmentFilter.setTypeFilterDeleted(FilterType.EQUAL);

        return departmentService.getAll(departmentSort, departmentFilter);
    }

    Page<ContactDto> searchContacts(int page, int pageSize, String searchText) {
        List<EmployeeDto> employeeDtos = employeeService.getEmployeesForContacts(page, pageSize, searchText);
    }
}

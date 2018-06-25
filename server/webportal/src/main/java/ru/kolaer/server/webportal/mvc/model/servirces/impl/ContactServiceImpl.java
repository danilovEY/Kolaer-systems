package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.converter.ContactConverter;
import ru.kolaer.server.webportal.mvc.model.dao.ContactDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.PlacementDao;
import ru.kolaer.server.webportal.mvc.model.dto.*;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.ContactService;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final PlacementDao placementDao;
    private final ContactDao contactDao;
    private final EmployeeDao employeeDao;
    private final ContactConverter contactConverter;
    private final DepartmentService departmentService;


    @Override
    public List<DepartmentDto> getAllDepartments() {
        DepartmentSort departmentSort = new DepartmentSort();
        departmentSort.setSortCode(SortType.ASC);

        DepartmentFilter departmentFilter = new DepartmentFilter();
        departmentFilter.setFilterDeleted(false);
        departmentFilter.setTypeFilterDeleted(FilterType.EQUAL);

        return departmentService.getAll(departmentSort, departmentFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDto> searchContacts(int page, int pageSize, String searchText) {

        List<EmployeeEntity> employeeDtos = employeeDao.findEmployeesForContacts(page, pageSize, searchText);
        long employeeCount = employeeDao.findCountEmployeesForContacts(searchText);

        return new Page<>(contactConverter.employeeToContact(employeeDtos), page, employeeCount, pageSize);
    }

    @Override
    @Transactional
    public ContactDto saveContact(long employeeId, ContactRequestDto contactDto) {
        EmployeeEntity employeeEntity = employeeDao.findById(employeeId);

        ContactEntity contactEntity = Optional
                .ofNullable(employeeEntity.getContact())
                .orElse(new ContactEntity());

        contactEntity = contactConverter.updateToModel(contactEntity, contactDto);

        Long contactId = contactDao.save(contactEntity).getId();
        if(!contactId.equals(employeeEntity.getContactId())) {
            employeeEntity.setContactId(contactId);
            employeeEntity = employeeDao.save(employeeEntity);
        }

        return contactConverter.employeeToContact(employeeEntity);

    }

    @Override
    public ContactDto getContactByEmployeeId(Long employeeId) {
        return contactConverter.employeeToContact(employeeDao.findById(employeeId));
    }
}

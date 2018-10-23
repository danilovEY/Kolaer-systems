package ru.kolaer.server.service.contact.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.service.contact.ContactType;
import ru.kolaer.server.service.contact.dto.ContactDto;
import ru.kolaer.server.service.contact.dto.ContactRequestDto;
import ru.kolaer.server.service.contact.entity.ContactEntity;
import ru.kolaer.server.service.contact.repository.ContactRepository;
import ru.kolaer.server.service.contact.converter.ContactConverter;
import department.service.DepartmentService;
import employee.repository.EmployeeRepository;
import placement.repository.PlacementRepository;
import ru.kolaer.server.webportal.common.dto.FilterType;
import ru.kolaer.server.webportal.common.dto.SortType;
import department.dto.DepartmentFilter;
import department.dto.DepartmentSort;
import employee.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final PlacementRepository placementDao;
    private final ContactRepository contactDao;
    private final EmployeeRepository employeeDao;
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
    @Transactional(readOnly = true)
    public ContactDto getContactByEmployeeId(Long employeeId) {
        return contactConverter.employeeToContact(employeeDao.findById(employeeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDto> getAllContactsByDepartment(int page, int pageSize, long depId, ContactType type) {
        ContactType contactType = Optional.ofNullable(type).orElse(ContactType.OTHER);

        List<EmployeeEntity> employees = employeeDao.findEmployeeByDepIdAndContactType(page, pageSize, depId, contactType);
        Long count = employeeDao.findCountEmployeeByDepIdAndContactType(depId, contactType);

        return new Page<>(contactConverter.employeeToContact(employees), page, count, pageSize);
    }
}

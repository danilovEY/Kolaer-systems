package ru.kolaer.server.contact.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.constant.assess.ContactAccessConstant;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.contact.model.entity.ContactEntity;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.contact.model.request.FindContactPageRequest;
import ru.kolaer.server.contact.repository.ContactRepository;
import ru.kolaer.server.contact.repository.ContactSpecifications;
import ru.kolaer.server.core.model.dto.concact.ContactDetailsDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;
import ru.kolaer.server.employee.service.DepartmentService;
import ru.kolaer.server.placement.model.entity.PlacementEntity;
import ru.kolaer.server.placement.repository.PlacementRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final PlacementRepository placementRepository;
    private final ContactRepository contactRepository;
    private final EmployeeDao employeeDao;
    private final ContactMapper contactMapper;
    private final DepartmentService departmentService;
    private final AuthenticationService authenticationService;

    @Transactional(readOnly = true)
    public PageDto<ContactDetailsDto> searchContacts(int page, int pageSize, String searchText) {
        Set<Long> placementIds = placementRepository.findAllByName(searchText)
                .stream()
                .map(PlacementEntity::getId)
                .collect(Collectors.toSet());

        FindEmployeePageRequest findEmployeePageRequest = new FindEmployeePageRequest();
        findEmployeePageRequest.setQuery(searchText);

        Set<Long> employeeIds = employeeDao.findAllEmployee(findEmployeePageRequest)
                .stream()
                .map(EmployeeEntity::getId)
                .collect(Collectors.toSet());

        FindContactPageRequest findContactPageRequest = new FindContactPageRequest();
        findContactPageRequest.setEmployeeIds(employeeIds);
        findContactPageRequest.setPlacementIds(placementIds);
        findContactPageRequest.setQuery(searchText);

        Page<ContactEntity> contactPages = contactRepository.findAll(
                ContactSpecifications.findContacts(findContactPageRequest),
                findContactPageRequest.toPageRequest()
        );

        return new PageDto<>(
                contactMapper.convertToDto(contactPages.getContent()),
                page,
                contactPages.getTotalPages(),
                pageSize
        );
    }

    @Transactional
    public ContactDetailsDto saveContact(long employeeId, ContactRequestDto contactDto) {
        EmployeeEntity employee = employeeDao.findById(employeeId);

        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        if (!currentAccount.hasAccess(ContactAccessConstant.CONTACTS_WRITE)) {
            EmployeeEntity currentEmployee = employeeDao.findById(currentAccount.getEmployeeId());

            if (!employee.getDepartmentId().equals(currentEmployee.getDepartmentId())) {
                throw new AccessDeniedException("У вас нет доступа");
            }
        }

        ContactEntity contactEmployee = contactRepository.findByEmployeeId(employeeId)
                .orElse(new ContactEntity());

        ContactType contactType = Optional.ofNullable(contactEmployee.getType())
                .orElse(ContactType.MAIN);

        ContactEntity updateContact = contactMapper.convertToModel(contactDto);
        updateContact.setId(contactEmployee.getId());
        updateContact.setEmployeeId(employeeId);
        updateContact.setType(contactType);

        return contactMapper.convertToDto(Collections.singletonList(contactRepository.save(updateContact)))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ContactDetailsDto getContactByEmployeeId(Long employeeId) {
        return contactRepository.findByEmployeeId(employeeId)
                .map(Collections::singletonList)
                .map(contactMapper::convertToDto)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public PageDto<ContactDetailsDto> getAllContactsByDepartment(int pageNum, int pageSize, long depId, ContactType type) {
        Set<Long> employeeIds = employeeDao.findByDepartmentById(depId)
                .stream()
                .map(EmployeeEntity::getId)
                .collect(Collectors.toSet());

        FindContactPageRequest findContactPageRequest = new FindContactPageRequest();
        findContactPageRequest.setEmployeeIds(employeeIds);
        findContactPageRequest.setType(type);
        findContactPageRequest.setPageNum(pageNum);
        findContactPageRequest.setPageSize(pageSize);

        Page<ContactEntity> page = contactRepository.findAll(
                ContactSpecifications.findContacts(findContactPageRequest),
                findContactPageRequest.toPageRequest()
        );

        return new PageDto<>(
                contactMapper.convertToDto(page.getContent()),
                pageNum,
                page.getTotalPages(),
                pageSize
        );
    }
}

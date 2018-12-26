package ru.kolaer.server.contact.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;

import java.util.List;

public interface ContactService {
    List<DepartmentDto> getAllDepartments();

    Page<ContactDto> searchContacts(int page, int pageSize, String searchText);

    ContactDto saveContact(long employeeId, ContactRequestDto contactDto);

    ContactDto getContactByEmployeeId(Long employeeId);

    Page<ContactDto> getAllContactsByDepartment(int page, int pageSize, long depId, ContactType type);
}

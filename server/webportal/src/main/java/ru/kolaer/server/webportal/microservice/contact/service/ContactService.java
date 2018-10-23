package ru.kolaer.server.webportal.microservice.contact.service;

import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.microservice.contact.ContactType;
import ru.kolaer.server.webportal.microservice.contact.dto.ContactDto;
import ru.kolaer.server.webportal.microservice.contact.dto.ContactRequestDto;

import java.util.List;

public interface ContactService {
    List<DepartmentDto> getAllDepartments();

    Page<ContactDto> searchContacts(int page, int pageSize, String searchText);

    ContactDto saveContact(long employeeId, ContactRequestDto contactDto);

    ContactDto getContactByEmployeeId(Long employeeId);

    Page<ContactDto> getAllContactsByDepartment(int page, int pageSize, long depId, ContactType type);
}

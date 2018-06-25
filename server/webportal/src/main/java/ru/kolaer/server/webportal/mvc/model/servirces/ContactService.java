package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.ContactDto;
import ru.kolaer.server.webportal.mvc.model.dto.ContactRequestDto;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactType;

import java.util.List;

public interface ContactService {
    List<DepartmentDto> getAllDepartments();

    Page<ContactDto> searchContacts(int page, int pageSize, String searchText);

    ContactDto saveContact(long employeeId, ContactRequestDto contactDto);

    ContactDto getContactByEmployeeId(Long employeeId);

    Page<ContactDto> getAllContactsByDepartment(int page, int pageSize, long depId, ContactType type);
}

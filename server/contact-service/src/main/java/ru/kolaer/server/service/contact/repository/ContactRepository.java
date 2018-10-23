package ru.kolaer.server.service.contact.repository;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.service.contact.entity.ContactEntity;

import java.util.List;

public interface ContactRepository extends DefaultRepository<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);

    ContactEntity findByEmployeeId(long employeeId);
}

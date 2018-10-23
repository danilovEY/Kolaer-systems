package ru.kolaer.server.webportal.microservice.contact.repository;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.contact.entity.ContactEntity;

import java.util.List;

public interface ContactRepository extends DefaultRepository<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);

    ContactEntity findByEmployeeId(long employeeId);
}

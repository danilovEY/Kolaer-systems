package ru.kolaer.server.webportal.microservice.contact;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

public interface ContactRepository extends DefaultRepository<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);

    ContactEntity findByEmployeeId(long employeeId);
}

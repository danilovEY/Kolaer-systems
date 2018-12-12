package ru.kolaer.server.webportal.model.dao;


import ru.kolaer.server.webportal.model.entity.contact.ContactEntity;

import java.util.List;

public interface ContactDao extends DefaultDao<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);

    ContactEntity findByEmployeeId(long employeeId);
}

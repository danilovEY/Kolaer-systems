package ru.kolaer.server.contact.dao;


import ru.kolaer.server.contact.model.entity.ContactEntity;
import ru.kolaer.server.core.dao.DefaultDao;

import java.util.List;

public interface ContactDao extends DefaultDao<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);

    ContactEntity findByEmployeeId(long employeeId);
}

package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactEntity;

import java.util.List;

public interface ContactDao extends DefaultDao<ContactEntity> {
    List<ContactEntity> searchContact(String searchText);
}

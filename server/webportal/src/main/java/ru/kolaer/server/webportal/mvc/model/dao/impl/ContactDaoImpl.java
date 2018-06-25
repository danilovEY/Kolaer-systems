package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ContactDao;
import ru.kolaer.server.webportal.mvc.model.entities.contact.ContactEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import java.util.List;

@Repository
public class ContactDaoImpl extends AbstractDefaultDao<ContactEntity> implements ContactDao {

    protected ContactDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ContactEntity.class);
    }

    @Override
    public List<ContactEntity> searchContact(String searchText) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " WHERE email LIKE :searchText OR " +
                        "workPhoneNumber LIKE :searchText OR " +
                        "pager LIKE :searchText OR " +
                        "placement.name LIKE :searchText",
                        getEntityClass())
                .setParameter("searchText", "%" + searchText + "%")
                .list();
    }

    @Override
    public ContactEntity findByEmployeeId(long employeeId) {
        return getSession()
                .createQuery("SELECT contact FROM " + getEntityName(EmployeeEntity.class) + " WHERE id = :employeeId",
                        getEntityClass())
                .setParameter("employeeId", employeeId)
                .uniqueResultOptional()
                .orElse(null);
    }
}

package ru.kolaer.server.webportal.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.ContactDao;
import ru.kolaer.server.webportal.model.entity.contact.ContactEntity;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class ContactDaoImpl extends AbstractDefaultDao<ContactEntity> implements ContactDao {

    protected ContactDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, ContactEntity.class);
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

package ru.kolaer.server.webportal.microservice.contact;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.microservice.employee.EmployeeEntity;

import java.util.List;

@Repository
public class ContactRepositoryImpl extends AbstractDefaultRepository<ContactEntity> implements ContactRepository {

    protected ContactRepositoryImpl(SessionFactory sessionFactory) {
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

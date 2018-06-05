package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 04.04.2017.
 */
@Repository
public class BankAccountDaoImpl extends AbstractDefaultDao<BankAccountEntity> implements BankAccountDao {

    protected BankAccountDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, BankAccountEntity.class);
    }

    @Override
    public BankAccountEntity findByInitials(String initials) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " b WHERE b.employeeEntity.initials = :initials", getEntityClass())
                .setParameter("initials", initials)
                .list()
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public Integer getCountAllAccount() {
        return getSession()
                .createQuery("SELECT count(id) FROM " + getEntityName(), Integer.class)
                .uniqueResult();
    }

    @Override
    public BankAccountEntity findByEmployeeId(Long employeeId) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " b WHERE b.employeeId = :employeeId", BankAccountEntity.class)
                .setParameter("employeeId", employeeId)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public List<Long> findAllEmployeeIds() {
        return getSession()
                .createQuery("SELECT (employeeId) FROM " + getEntityName(), Long.class)
                .list();
    }

    @Override
    public Optional<BankAccountEntity> findByCheck(String check) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " b WHERE b.check = :check", BankAccountEntity.class)
                .setParameter("check", check)
                .uniqueResultOptional();
    }
}

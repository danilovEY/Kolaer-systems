package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccount;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 04.04.2017.
 */
@Repository
public class BankAccountDaoImpl implements BankAccountDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public BankAccount findByInitials(String initials) {
        return (BankAccount) this.sessionFactory.getCurrentSession()
                .createQuery("FROM BankAccount b WHERE b.employeeEntity.initials = :initials")
                .setParameter("initials", initials)
                .list().get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCountAllAccount() {
        return (Integer) this.sessionFactory.getCurrentSession()
                .createQuery("SELECT count(id) FROM BankAccount")
                .uniqueResult();
    }

    @Override
    @Transactional
    public void updateOrSave(List<BankAccount> bankAccountList) {
        final int defaultBachSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
        final Session currentSession = this.sessionFactory.getCurrentSession();

        int i = 0;
        for (BankAccount bankAccount : bankAccountList) {
            currentSession.saveOrUpdate(bankAccount);

            if(i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> findAll() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM BankAccount")
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount findByID(@NonNull Integer id) {
        return (BankAccount) this.sessionFactory.getCurrentSession()
                .createQuery("FROM BankAccount b WHERE b.id = :id")
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    @Transactional
    public void persist(@NonNull BankAccount obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull BankAccount obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void delete(@NonNull List<BankAccount> objs) {
        this.sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM BankAccount b WHERE b.id IN (:ids)")
                .setParameter("ids", objs.stream().map(BankAccount::getId).collect(Collectors.toList()))
                .executeUpdate();
    }

    @Override
    @Transactional
    public void update(@NonNull BankAccount obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    @Transactional
    public void update(@NonNull List<BankAccount> objs) {
        final int defaultBachSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
        final Session currentSession = this.sessionFactory.getCurrentSession();

        int i = 0;
        for (BankAccount bankAccount : objs) {
            currentSession.update(bankAccount);

            if(i % defaultBachSize == 0) {
                i = 0;
                currentSession.flush();
                currentSession.clear();
            }
        }
    }
}

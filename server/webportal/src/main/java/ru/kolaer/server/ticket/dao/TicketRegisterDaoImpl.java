package ru.kolaer.server.ticket.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.ticket.model.entity.TicketRegisterEntity;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Repository
public class TicketRegisterDaoImpl extends AbstractDefaultDao<TicketRegisterEntity> implements TicketRegisterDao {

    protected TicketRegisterDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, TicketRegisterEntity.class);
    }

    @Override
    public List<TicketRegisterEntity> findAllByDepName(String depName) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " tr WHERE tr.department.name = :depName", getEntityClass())
                .setParameter("depName", depName)
                .list();
    }

    @Override
    public List<TicketRegisterEntity> findAllByDepName(int number, int pageSize, String depName) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " tr WHERE tr.department.name = :depName", getEntityClass())
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("depName", depName)
                .list();
    }

    @Override
    public Long findCountAllByDepName(int number, int pageSize, String depName) {
        return getSession()
                .createQuery("SELECT COUNT(tr.id) FROM " + getEntityName() + " tr WHERE tr.department.name = :depName", Long.class)
                .setParameter("depName", depName)
                .uniqueResult();
    }

    @Override
    public List<TicketRegisterEntity> getTicketRegisterByDateAndDep(Date date, String depName) {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " tr WHERE MONTH(tr.createRegister) = MONTH(:createRegister)" +
                        " AND YEAR(tr.createRegister) = YEAR(:createRegister)" +
                        " AND tr.department.name = :depName", getEntityClass())
                .setParameter("createRegister", date)
                .setParameter("depName", depName)
                .list();
    }

    @Override
    public List<TicketRegisterEntity> findAllOpenRegister() {
        return getSession()
                .createQuery("FROM " + getEntityName() + " tr WHERE tr.close = false", getEntityClass())
                .list();
    }

    @Override
    public List<TicketRegisterEntity> findIncludeAllOnLastMonth() {
        return getSession()
                .createQuery("FROM " + getEntityName() +
                        " tr WHERE tr.includeAll = TRUE AND " +
                        "MONTH(tr.createRegister) = MONTH(CURRENT_TIMESTAMP) AND " +
                        "DAY(create_register) >= 26 ", getEntityClass())
                .list();
    }
}

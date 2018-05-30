package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Repository
public class TicketDaoImpl extends AbstractDefaultDao<TicketEntity> implements TicketDao {

    protected TicketDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, TicketEntity.class);
    }

    @Override
    public List<TicketEntity> findAllByRegisterId(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " t WHERE t.registerId = :id", getEntityClass())
                .setParameter("id", id)
                .list();
    }

    @Override
    public int deleteByRegisterId(Long regId) {
        return getSession()
                .createQuery("DELETE FROM " + getEntityName() + " t WHERE t.registerId = :id")
                .setParameter("id", regId)
                .executeUpdate();
    }
}

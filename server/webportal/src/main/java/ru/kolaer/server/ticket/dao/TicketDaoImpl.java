package ru.kolaer.server.ticket.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.ticket.model.entity.TicketEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Repository
public class TicketDaoImpl extends AbstractDefaultDao<TicketEntity> implements TicketDao {

    protected TicketDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, TicketEntity.class);
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

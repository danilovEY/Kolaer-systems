package ru.kolaer.server.webportal.microservice.ticket;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Repository
public class TicketRepositoryImpl extends AbstractDefaultRepository<TicketEntity> implements TicketRepository {

    protected TicketRepositoryImpl(SessionFactory sessionFactory) {
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

package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
@Repository
public class ChatMessageDaoImpl extends AbstractDefaultDao<ChatMessageEntity> implements ChatMessageDao {
    protected ChatMessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ChatMessageEntity.class);
    }

    @Override
    public List<ChatMessageEntity> findAllByRoom(String room, Integer number, Integer pageSize) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE room = :room ORDER BY id DESC", getEntityClass())
                .setParameter("room", room)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public List<ChatMessageEntity> findAllByRoom(String room) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE room = :room ORDER BY id DESC", getEntityClass())
                .setParameter("room", room)
                .list();
    }

    @Override
    public Long findCountByRoom(String room) {
        return getSession().createQuery("SELECT COUNT(id) FROM " + getEntityName() + " WHERE room = :room", Long.class)
                .setParameter("room", room)
                .uniqueResult();
    }
}

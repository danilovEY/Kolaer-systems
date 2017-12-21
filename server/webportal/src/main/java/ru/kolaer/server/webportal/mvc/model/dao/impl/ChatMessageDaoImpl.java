package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public List<ChatMessageEntity> findAllByRoom(String room, boolean withHide, Integer number, Integer pageSize) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE room = :room AND hide = :hide ORDER BY id DESC", getEntityClass())
                .setParameter("room", room)
                .setParameter("hide", withHide)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageEntity> findAllByRoom(String room, boolean withHide) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE room = :room AND hide = :hide ORDER BY id DESC",
                getEntityClass())
                .setParameter("room", room)
                .setParameter("hide", withHide)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findCountByRoom(String room, boolean withHide) {
        return getSession().createQuery("SELECT COUNT(id) FROM " + getEntityName() + " WHERE room = :room AND hide = :hide",
                Long.class)
                .setParameter("room", room)
                .setParameter("hide", withHide)
                .uniqueResult();
    }

    @Override
    @Transactional
    public void setHideOnIds(List<Long> ids, boolean hide) {
        getSession().createQuery("UPDATE FROM " + getEntityName() + " SET hide = :hide WHERE id IN (:ids)")
                .setParameterList("ids", ids)
                .setParameter("hide", hide)
                .executeUpdate();
    }
}

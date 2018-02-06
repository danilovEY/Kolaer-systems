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
    public List<ChatMessageEntity> findAllByRoom(Long roomId, boolean withHide, Integer number, Integer pageSize) {
        String sql = "FROM " + getEntityName() + " WHERE roomId = :room";
        if(!withHide) {
            sql += " AND hide = false";
        }

        sql += " ORDER BY id DESC";
        return getSession().createQuery(sql, getEntityClass())
                .setParameter("room", roomId)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageEntity> findAllByRoom(Long roomId, boolean withHide) {
        String sql = "FROM " + getEntityName() + " WHERE roomId = :room";
        if(!withHide) {
            sql += " AND hide = false";
        }

        sql += " ORDER BY id DESC";
        return getSession().createQuery(sql, getEntityClass())
                .setParameter("room", roomId)
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findCountByRoom(Long roomId, boolean withHide) {
        String sql = "SELECT COUNT(id) FROM " + getEntityName() + " WHERE roomId = :room";
        if(!withHide) {
            sql += " AND hide = false";
        }

        sql += " ORDER BY id DESC";
        return getSession().createQuery(sql, Long.class)
                .setParameter("room", roomId)
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

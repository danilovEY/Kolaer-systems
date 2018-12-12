package ru.kolaer.server.webportal.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.ChatMessageDao;
import ru.kolaer.server.webportal.model.entity.chat.ChatMessageEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 08.11.2017.
 */
@Repository
public class ChatMessageDaoImpl extends AbstractDefaultDao<ChatMessageEntity> implements ChatMessageDao {
    protected ChatMessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ChatMessageEntity.class);
    }

    @Override
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
    public void markHideOnIds(List<Long> ids, boolean hide) {
        getSession().createQuery("UPDATE FROM " + getEntityName() + " SET hide = :hide WHERE id IN (:ids)")
                .setParameterList("ids", ids)
                .setParameter("hide", hide)
                .executeUpdate();
    }

    @Override
    public void markRead(List<Long> messageIds, Long accountId, boolean read) {
        if(read) {
            for (Long messageId : messageIds) {
                getSession().createNativeQuery("INSERT INTO chat_message_read (message_id, account_id) VALUE (:messageId, :accountId)")
                        .setParameter("messageId", messageId)
                        .setParameter("accountId", accountId)
                        .executeUpdate();
            }
        } else {
            getSession().createNativeQuery("DELETE FROM chat_message_read WHERE message_id IN(:messageIds) AND account_id = :accountId")
                    .setParameter("messageIds", messageIds)
                    .setParameter("accountId", accountId)
                    .executeUpdate();
        }
    }

    @Override
    public List<Long> findReadByMessageAndAccount(List<Long> messageIds, Long accountId) {
        return getSession()
                .createNativeQuery("SELECT message_id as id FROM chat_message_read WHERE message_id IN(:messageIds) AND account_id = :accountId", ChatMessageEntity.class)
                .setParameter("messageIds", messageIds)
                .setParameter("accountId", accountId)
                .list()
                .stream()
                .map(ChatMessageEntity::getId)
                .collect(Collectors.toList());
    }
}

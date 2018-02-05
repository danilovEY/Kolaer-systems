package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatRoomDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatRoomEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
@Repository
public class ChatRoomDaoImpl extends AbstractDefaultDao<ChatRoomEntity> implements ChatRoomDao {
    protected ChatRoomDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ChatRoomEntity.class);
    }

    @Override
    public void addAccountToRoom(Long roomId, Long userId) {
        getSession().createNamedQuery("INSERT INTO chat_room_account (chat_room_id, account_id) VALUES(:roomId, userId)")
                .setParameter("roomId", roomId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public boolean containsKey(String roomKey) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE roomKey = :roomKey")
                .setParameter("roomKey", roomKey)
                .list()
                .size() > 0;
    }

    @Override
    public ChatRoomEntity findByRoomKey(String roomKey) {
        return getSession().createQuery("FROM " + getEntityName() + " WHERE roomKey = :roomKey", getEntityClass())
                .setParameter("roomKey", roomKey)
                .list()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ChatRoomEntity> findAllByUserInRoom(Long id) {
        return getSession().createQuery("FROM " + getEntityName() + " AS chatRoom WHERE :accountId IN elements(chatRoom.accountIds)", getEntityClass())
                .setParameter("accountId", id)
                .list();
    }
}

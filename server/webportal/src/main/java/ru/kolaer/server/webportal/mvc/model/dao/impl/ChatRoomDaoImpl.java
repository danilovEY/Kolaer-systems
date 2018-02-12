package ru.kolaer.server.webportal.mvc.model.dao.impl;

import javafx.util.Pair;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.ChatRoomDao;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatRoomEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        getSession().createNativeQuery("INSERT INTO chat_room_account (chat_room_id, account_id) VALUES(:roomId, :userId)")
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
    public List<ChatRoomEntity> findAllByUser(Long id) {
        return getSession().createQuery("FROM " + getEntityName() + " AS chatRoom WHERE :accountId IN elements(chatRoom.accountIds)", getEntityClass())
                .setParameter("accountId", id)
                .list();
    }

    @Override
    public Map<Long, List<Long>> findAllUsersByRooms(List<Long> roomIds) {
        List<Object[]> results = getSession().createNativeQuery("SELECT chat_room_id, account_Id FROM chat_room_account WHERE chat_room_id IN (:roomIds)")
                .setParameterList("roomIds", roomIds)
                .list();

        return results.stream()
                .map(objects -> new Pair<>(((BigInteger) objects[0]).longValue(), ((BigInteger) objects[1]).longValue()))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
    }

    @Override
    public void removeUserFromRooms(List<Long> roomIds, Long accountId) {
        getSession().createNativeQuery("DELETE FROM chat_room_account WHERE account_Id = :accountId AND chat_room_id IN(:roomIds)")
                .setParameter("accountId", accountId)
                .setParameterList("roomIds", roomIds)
                .executeUpdate();
    }
}

package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.chat.ChatRoomEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomDao extends DefaultDao<ChatRoomEntity> {
    void addAccountToRoom(Long roomId, Long accountId);

    boolean containsKey(String roomKey);

    ChatRoomEntity findByRoomKey(String roomKey);

    List<ChatRoomEntity> findAllByUser(Long id);

    Map<Long, List<Long>> findAllUsersByRooms(List<Long> roomIds);

    void removeUserFromRooms(List<Long> roomIds, Long accountId);
}

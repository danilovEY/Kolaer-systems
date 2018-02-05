package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatRoomEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomDao extends DefaultDao<ChatRoomEntity> {
    void addAccountToRoom(Long roomId, Long accountId);

    boolean containsKey(String roomKey);

    ChatRoomEntity findByRoomKey(String roomKey);

    List<ChatRoomEntity> findAllByUserInRoom(Long id);
}

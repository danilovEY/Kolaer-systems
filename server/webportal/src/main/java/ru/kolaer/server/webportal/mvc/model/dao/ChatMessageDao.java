package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageDao extends DefaultDao<ChatMessageEntity> {
    List<ChatMessageEntity> findAllByRoom(Long roomId, boolean withHide, Integer number, Integer pageSize);
    default List<ChatMessageEntity> findAllByRoom(Long roomId, Integer number, Integer pageSize) {
        return findAllByRoom(roomId, false, number, pageSize);
    }

    List<ChatMessageEntity> findAllByRoom(Long roomId, boolean withHide);
    default List<ChatMessageEntity> findAllByRoom(Long roomId) {
        return this.findAllByRoom(roomId, false);
    }

    default Long findCountByRoom(Long roomId) {
        return findCountByRoom(roomId, false);
    }

    Long findCountByRoom(Long roomId, boolean withHide);

    void setHideOnIds(List<Long> ids, boolean hide);
}

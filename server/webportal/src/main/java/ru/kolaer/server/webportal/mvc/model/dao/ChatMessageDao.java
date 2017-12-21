package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageDao extends DefaultDao<ChatMessageEntity> {
    List<ChatMessageEntity> findAllByRoom(String room, boolean withHide, Integer number, Integer pageSize);
    default List<ChatMessageEntity> findAllByRoom(String room, Integer number, Integer pageSize) {
        return findAllByRoom(room, false, number, pageSize);
    }

    List<ChatMessageEntity> findAllByRoom(String room, boolean withHide);
    default List<ChatMessageEntity> findAllByRoom(String room) {
        return this.findAllByRoom(room, false);
    }

    default Long findCountByRoom(String room) {
        return findCountByRoom(room, false);
    }

    Long findCountByRoom(String room, boolean withHide);

    void setHideOnIds(List<Long> ids, boolean hide);
}

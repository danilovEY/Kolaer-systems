package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageDao extends DefaultDao<ChatMessageEntity> {
    List<ChatMessageEntity> findAllByRoom(String room, Integer number, Integer pageSize);
    List<ChatMessageEntity> findAllByRoom(String room);

    Long findCountByRoom(String room);
}

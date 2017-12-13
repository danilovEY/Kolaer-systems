package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageService extends DefaultService<ChatMessageDto> {
    List<ChatMessageDto> getAllByRoom(String room);

    Page<ChatMessageDto> getAllByRoom(String roomId, Integer number, Integer pageSize);
}

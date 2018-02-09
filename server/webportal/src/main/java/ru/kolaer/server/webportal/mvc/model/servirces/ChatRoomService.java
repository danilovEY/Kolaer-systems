package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoMessageActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomService extends DefaultService<ChatRoomDto>, ChatService {
    void send(ChatInfoRoomActionDto chatInfoCreateNewRoomDto);

    void send(ChatInfoMessageActionDto chatInfoRoomActionDto);
}
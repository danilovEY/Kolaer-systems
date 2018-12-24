package ru.kolaer.server.webportal.service;

import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoMessageActionDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.server.core.service.DefaultService;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomService extends DefaultService<ChatRoomDto>, ChatService {
    void send(ChatInfoRoomActionDto chatInfoCreateNewRoomDto);

    void send(ChatInfoMessageActionDto chatInfoRoomActionDto);
}
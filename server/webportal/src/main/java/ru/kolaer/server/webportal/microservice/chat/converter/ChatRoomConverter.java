package ru.kolaer.server.webportal.microservice.chat.converter;

import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.chat.entity.ChatRoomEntity;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomConverter extends BaseConverter<ChatRoomDto, ChatRoomEntity> {
}

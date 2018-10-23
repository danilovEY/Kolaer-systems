package ru.kolaer.server.service.chat.converter;

import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.server.service.chat.entity.ChatRoomEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomConverter extends BaseConverter<ChatRoomDto, ChatRoomEntity> {
}

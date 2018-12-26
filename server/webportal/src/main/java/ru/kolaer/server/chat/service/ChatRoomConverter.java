package ru.kolaer.server.chat.service;

import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.server.chat.model.entity.ChatRoomEntity;
import ru.kolaer.server.core.converter.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatRoomConverter extends BaseConverter<ChatRoomDto, ChatRoomEntity> {
}

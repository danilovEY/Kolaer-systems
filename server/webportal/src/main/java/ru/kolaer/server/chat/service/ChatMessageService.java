package ru.kolaer.server.chat.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.core.service.DefaultService;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageService extends DefaultService<ChatMessageDto> {
    List<ChatMessageDto> getAllByRoom(Long roomId);

    PageDto<ChatMessageDto> getAllByRoom(Long roomId, Integer number, Integer pageSize);
}

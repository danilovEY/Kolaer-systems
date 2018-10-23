package ru.kolaer.server.webportal.microservice.chat.service;

import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;

import java.util.List;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageService extends DefaultService<ChatMessageDto> {
    List<ChatMessageDto> getAllByRoom(Long roomId);

    Page<ChatMessageDto> getAllByRoom(Long roomId, Integer number, Integer pageSize);
}

package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;

/**
 * Created by danilovey on 06.02.2018.
 */
public interface ChatHandler {
    default void handlerInfo(ChatInfoRoomActionDto chatInfoDto){}
    default void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {}
    default void handlerMessage(ChatMessageDto chatMessageDto) {}
}

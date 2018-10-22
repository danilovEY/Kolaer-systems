package ru.kolaer.client.chat.service;

import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatInfoMessageActionDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;

/**
 * Created by danilovey on 06.02.2018.
 */
public interface ChatHandler {
    default void handlerInfo(ChatInfoRoomActionDto chatInfoDto){}
    default void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {}
    default void handlerInfo(ChatInfoMessageActionDto infoMessageActionDto) {}
    default void handlerMessage(ChatMessageDto chatMessageDto) {}
}

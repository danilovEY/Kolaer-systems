package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.chat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ChatUserDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatService extends DefaultService<ChatGroupDto> {
    ChatUserDto addActiveUser(ChatUserDto dto);
    void removeActiveUser(ChatUserDto dto);
    ChatGroupDto getMainGroup();
}

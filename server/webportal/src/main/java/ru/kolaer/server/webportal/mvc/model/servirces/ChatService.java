package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatService extends DefaultService<ChatGroupDto> {
    ChatUserDto addActiveUserToMain(ChatUserDto dto);
    void removeActiveUserFromMain(ChatUserDto dto);

    ChatUserDto getUser(String sessionId);

    ChatUserDto getUserByAccountId(Long id);

    ChatGroupDto getOrCreatePrivateGroup(String name);

    ChatGroupDto getOrCreatePublicGroup(String name);

    void removeFromAllGroup(ChatUserDto chatUserDto);

    ChatGroupDto createGroup(String name);
    ChatGroupDto getGroup(String name);

    ChatGroupDto getOrCreatePrivateGroupByAccountId(IdsDto idsDto);
}

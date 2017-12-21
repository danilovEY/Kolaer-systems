package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatService extends DefaultService<ChatGroupDto> {
    ChatUserDto addActiveUserToMain(ChatUserDto dto);
    void removeActiveUserFromMain(ChatUserDto dto);

    ChatUserDto getUser(String sessionId);

    ChatUserDto getUserByAccountId(Long id);

    void removeFromAllGroup(ChatUserDto chatUserDto);

    ChatGroupDto createGroup(String name);

    void send(ChatInfoDto chatInfoDto);

    void send(String roomId, ChatInfoDto chatInfoDto);

    void sendDisconnect(String sessionId);

    void send(ChatMessageDto message);

    ChatGroupDto createPrivateGroup(String name, IdsDto idsDto);
    ChatGroupDto createPublicGroup(String name, IdsDto idsDto);

    ChatGroupDto getByRoomId(String roomId);

    List<ChatGroupDto> getAllForUser();

    ChatGroupDto getMainGroup();

    void hideMessage(IdsDto idsDto, boolean hide);
}

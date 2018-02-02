package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatService extends DefaultService<ChatRoomDto> {
    ChatUserDto addActiveUser(ChatUserDto dto);
    void removeActiveUser(ChatUserDto dto);

    ChatUserDto getUser(String sessionId);

    ChatUserDto getUserByAccountId(Long id);

    ChatRoomDto createGroup(String name);

    void send(ChatInfoDto chatInfoDto);

    void send(String roomId, ChatInfoDto chatInfoDto);

    void sendDisconnect(String sessionId);

    void send(ChatMessageDto message);

    ChatRoomDto createSingleGroup(IdDto accountId);
    ChatRoomDto createPrivateGroup(String name, IdsDto idsDto);
    ChatRoomDto createPublicGroup(String name, IdsDto idsDto);

    List<ChatRoomDto> getAllForUser();

    ChatRoomDto getMainGroup();

    void hideMessage(IdsDto idsDto, boolean hide);

    ChatUserDto createChatUserDto(AccountDto accountDto);
}

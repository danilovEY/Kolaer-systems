package ru.kolaer.server.webportal.microservice.chat;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.IdDto;
import ru.kolaer.common.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;

import java.util.Collection;
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

    void send(ChatInfoUserActionDto chatInfoUserActionDto);
    void sendDisconnect(String sessionId);
    void send(ChatMessageDto message);

    ChatRoomDto createSingleGroup(IdDto accountId);
    List<ChatRoomDto> createSingleGroup(IdsDto idsDto);
    ChatRoomDto createPrivateGroup(String name, IdsDto idsDto);
    ChatRoomDto createPublicGroup(String name, IdsDto idsDto);

    void quitFromRooms(IdsDto idsDto);

    List<ChatRoomDto> getAllRoomForAuthUser();

    void markReadMessages(IdsDto idsDto, boolean read);
    void hideMessage(IdsDto idsDto, boolean hide);
    void deleteMessage(IdsDto idsDto);

    ChatUserDto createChatUserDto(AccountDto accountDto);

    Collection<ChatUserDto> getOnlineUsers();

    ChatUserDto getOrCreateUserByAccountId(Long accountId);
    List<ChatUserDto> getOrCreateUserByAccountId(List<Long> accountIds);
}

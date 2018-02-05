package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.IdDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatTable {
    ServerResponse<List<ChatRoomDto>> getRooms();

    ServerResponse<ChatUserDto> getActiveByIdAccount(Long id);

    ServerResponse<ChatRoomDto> createPrivateRoom(IdsDto idsDto, String name);
    ServerResponse<ChatRoomDto> createSingleRoom(IdDto idDto);
    ServerResponse<ChatRoomDto> getRoomById(long roomId);
    ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(long roomId);

    ServerResponse hideMessage(IdsDto idsDto);
}

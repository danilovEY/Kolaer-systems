package ru.kolaer.client.core.system.network;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.IdDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatTable {
    ServerResponse<List<ChatRoomDto>> getRooms();
    ServerResponse<List<ChatUserDto>> getOnlineUser();

    ServerResponse<ChatUserDto> getActiveByIdAccount(Long id);

    ServerResponse<ChatRoomDto> createPrivateRoom(IdsDto idsDto, String name);
    ServerResponse<ChatRoomDto> createSingleRoom(IdDto idDto);
    ServerResponse quitFromRoom(IdsDto idsDto);

    ServerResponse<List<ChatRoomDto>> createSingleRooms(IdsDto idDto);

    ServerResponse<ChatRoomDto> getRoomById(long roomId);
    ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(long roomId);

    ServerResponse hideMessage(IdsDto idsDto);
    ServerResponse deleteMessage(IdsDto idsDto);
    ServerResponse markAsReadMessage(IdsDto idsDto);
}

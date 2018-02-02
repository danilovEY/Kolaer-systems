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
    ServerResponse<List<ChatRoomDto>> getActiveGroup();

    ServerResponse<ChatUserDto> getActiveByIdAccount(Long id);

    ServerResponse<ChatRoomDto> createPrivateGroup(IdsDto idsDto, String name);
    ServerResponse<ChatRoomDto> createSingleGroup(IdDto idDto);
    ServerResponse<ChatRoomDto> getGroupByRoomId(long roomId);
    ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(long roomId);

    ServerResponse hideMessage(IdsDto idsDto);
}

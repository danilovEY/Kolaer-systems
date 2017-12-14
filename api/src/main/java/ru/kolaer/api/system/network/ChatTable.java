package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatTable {
    ServerResponse<List<ChatGroupDto>> getActiveGroup();

    ServerResponse<ChatUserDto> getActiveByIdAccount(Long id);

    ServerResponse<ChatGroupDto> createPrivateGroup(IdsDto idsDto, String name);
    ServerResponse<ChatGroupDto> getGroupByRoomId(String roomId);
    ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(String roomId);
}

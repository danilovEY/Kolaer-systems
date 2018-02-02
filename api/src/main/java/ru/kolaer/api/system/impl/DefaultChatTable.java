package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.IdDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.network.ChatTable;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public class DefaultChatTable implements ChatTable {
    @Override
    public ServerResponse<List<ChatRoomDto>> getActiveGroup() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatUserDto> getActiveByIdAccount(Long id) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> createPrivateGroup(IdsDto idsDto, String name) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> createSingleGroup(IdDto idDto) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> getGroupByRoomId(long roomId) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<Page<ChatMessageDto>> getMessageByRoomId(long roomId) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse hideMessage(IdsDto idsDto) {
        return ServerResponse.createServerResponse();
    }
}

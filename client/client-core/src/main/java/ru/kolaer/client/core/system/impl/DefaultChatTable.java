package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.network.ChatTable;
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
public class DefaultChatTable implements ChatTable {
    @Override
    public ServerResponse<List<ChatRoomDto>> getRooms() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<List<ChatUserDto>> getOnlineUser() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatUserDto> getActiveByIdAccount(Long id) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> createPrivateRoom(IdsDto idsDto, String name) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> createSingleRoom(IdDto idDto) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse quitFromRoom(IdsDto idsDto) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<List<ChatRoomDto>> createSingleRooms(IdsDto idDto) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<ChatRoomDto> getRoomById(long roomId) {
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

    @Override
    public ServerResponse deleteMessage(IdsDto idsDto) {
        return null;
    }

    @Override
    public ServerResponse markAsReadMessage(IdsDto idsDto) {
        return ServerResponse.createServerResponse();
    }
}

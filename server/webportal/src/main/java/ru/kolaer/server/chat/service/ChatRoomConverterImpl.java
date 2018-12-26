package ru.kolaer.server.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.chat.model.entity.ChatRoomEntity;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 08.11.2017.
 */
@Component
@Slf4j
public class ChatRoomConverterImpl implements ChatRoomConverter {

    @Override
    public ChatRoomEntity convertToModel(ChatRoomDto dto) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setId(dto.getId());
        chatRoomEntity.setName(dto.getName());
        chatRoomEntity.setRoomKey(dto.getRoomKey());
        chatRoomEntity.setType(dto.getType());

        Optional.ofNullable(dto.getUserCreated())
                .map(ChatUserDto::getAccountId)
                .ifPresent(chatRoomEntity::setUserCreatedId);

        Optional.ofNullable(dto.getUsers())
                .map(users -> users.stream().map(ChatUserDto::getAccountId).collect(Collectors.toList()))
                .ifPresent(chatRoomEntity::setAccountIds);

        return chatRoomEntity;
    }

    @Override
    public ChatRoomDto convertToDto(ChatRoomEntity model) {
        if(model == null) {
            return null;
        }

        return updateData(new ChatRoomDto(), model);
    }

    @Override
    public ChatRoomDto updateData(ChatRoomDto oldDto, ChatRoomEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());
        oldDto.setType(newModel.getType());
        oldDto.setRoomKey(newModel.getRoomKey());

        return oldDto;
    }
}

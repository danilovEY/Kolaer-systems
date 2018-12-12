package ru.kolaer.server.webportal.model.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.model.entity.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.model.servirce.ChatService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 08.11.2017.
 */
@Component
@Slf4j
public class ChatMessageConverterImpl implements ChatMessageConverter {
    private final ChatService chatService;

    public ChatMessageConverterImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public ChatMessageEntity convertToModel(ChatMessageDto dto) {
        if(dto == null) {
            return null;
        }

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setId(dto.getId());
        chatMessageEntity.setRoomId(dto.getRoomId());
        chatMessageEntity.setMessage(dto.getMessage());
        chatMessageEntity.setCreateMessage(dto.getCreateMessage());
        chatMessageEntity.setType(dto.getType());
        Optional.ofNullable(dto.getFromAccount())
                .map(ChatUserDto::getAccountId)
                .ifPresent(chatMessageEntity::setAccountId);
        return chatMessageEntity;
    }

    @Override
    public ChatMessageDto convertToDto(ChatMessageEntity model) {
        if(model == null) {
            return null;
        }

        return updateData(convertToDtoWithOutSubEntity(model), model);
    }

    @Override
    public ChatMessageDto updateData(ChatMessageDto oldDto, ChatMessageEntity newModel) {
        Optional.ofNullable(newModel.getAccountId())
                .map(chatService::getOrCreateUserByAccountId)
                .ifPresent(oldDto::setFromAccount);
        return oldDto;
    }

    @Override
    public List<ChatMessageDto> convertToDto(List<ChatMessageEntity> model) {
        List<Long> accountIds = model.stream()
                .map(ChatMessageEntity::getAccountId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, ChatUserDto> usersMap = chatService.getOrCreateUserByAccountId(accountIds)
                .stream()
                .collect(Collectors.toMap(ChatUserDto::getAccountId, Function.identity()));

        return model.stream()
                .map(message -> {
                    ChatMessageDto dto = this.convertToDtoWithOutSubEntity(message);
                    dto.setFromAccount(usersMap.get(message.getAccountId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public ChatMessageDto convertToDtoWithOutSubEntity(ChatMessageEntity model) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(model.getId());
        dto.setMessage(model.getMessage());
        dto.setRoomId(model.getRoomId());
        dto.setCreateMessage(model.getCreateMessage());
        dto.setType(model.getType());
        dto.setHide(model.isHide());

        return dto;
    }
}

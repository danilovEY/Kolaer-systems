package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;

import java.util.Optional;

/**
 * Created by danilovey on 08.11.2017.
 */
@Component
@Slf4j
public class ChatMessageConverterImpl implements ChatMessageConverter {
    private final AccountService accountConverter;

    public ChatMessageConverterImpl(AccountService accountConverter) {
        this.accountConverter = accountConverter;
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
                .map(AccountDto::getId)
                .ifPresent(chatMessageEntity::setAccountId);
        return chatMessageEntity;
    }

    @Override
    public ChatMessageDto convertToDto(ChatMessageEntity model) {
        if(model == null) {
            return null;
        }

        return updateData(new ChatMessageDto(), model);
    }

    @Override
    public ChatMessageDto updateData(ChatMessageDto oldDto, ChatMessageEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setMessage(newModel.getMessage());
        oldDto.setRoomId(newModel.getRoomId());
        oldDto.setCreateMessage(newModel.getCreateMessage());
        oldDto.setType(newModel.getType());
        Optional.ofNullable(newModel.getAccountId())
                .map(accountConverter::getById)
                .ifPresent(oldDto::setFromAccount);
        return oldDto;
    }
}

package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
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
        chatMessageEntity.setRoom(dto.getRoom());
        chatMessageEntity.setMessage(dto.getMessage());
        chatMessageEntity.setCreateMessage(dto.getCreateMessage());
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

        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setRoom(model.getRoom());
        chatMessageDto.setMessage(model.getMessage());
        chatMessageDto.setId(model.getId());
        chatMessageDto.setCreateMessage(model.getCreateMessage());
        chatMessageDto.setFromAccount(accountConverter.getById(model.getAccountId()));
        return chatMessageDto;
    }

    @Override
    public ChatMessageDto updateData(ChatMessageDto oldDto, ChatMessageEntity newModel) {
        oldDto.setMessage(newModel.getMessage());
        oldDto.setRoom(newModel.getRoom());
        oldDto.setId(newModel.getId());
        oldDto.setCreateMessage(newModel.getCreateMessage());
        oldDto.setFromAccount(accountConverter.getById(newModel.getAccountId()));
        return oldDto;
    }
}

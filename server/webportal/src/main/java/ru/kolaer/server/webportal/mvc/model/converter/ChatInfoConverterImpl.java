package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatInfoEntity;

/**
 * Created by danilovey on 20.11.2017.
 */
@Service
public class ChatInfoConverterImpl implements ChatInfoConverter {
    @Override
    public ChatInfoEntity convertToModel(ChatInfoDto dto) {
        ChatInfoEntity chatInfoEntity = new ChatInfoEntity();
        chatInfoEntity.setId(dto.getId());
        chatInfoEntity.setAccountId(dto.getAccountId());
        chatInfoEntity.setToAccountId(dto.getToAccountId());
        chatInfoEntity.setCommand(dto.getCommand());
        chatInfoEntity.setCreateInfo(dto.getCreateInfo());
        chatInfoEntity.setData(dto.getData());
        return chatInfoEntity;
    }

    @Override
    public ChatInfoDto convertToDto(ChatInfoEntity model) {
        return updateData(new ChatInfoDto(), model);
    }

    @Override
    public ChatInfoDto updateData(ChatInfoDto oldDto, ChatInfoEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setAccountId(newModel.getAccountId());
        oldDto.setToAccountId(newModel.getToAccountId());
        oldDto.setCreateInfo(newModel.getCreateInfo());
        oldDto.setCommand(newModel.getCommand());
        oldDto.setData(newModel.getData());
        return oldDto;
    }
}

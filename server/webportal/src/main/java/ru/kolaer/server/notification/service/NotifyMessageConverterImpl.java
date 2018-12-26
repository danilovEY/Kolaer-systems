package ru.kolaer.server.notification.service;

import org.springframework.stereotype.Service;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.notification.model.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class NotifyMessageConverterImpl implements NotifyMessageConverter {
    @Override
    public NotifyMessageEntity convertToModel(NotifyMessageDto dto) {
        NotifyMessageEntity notifyMessageEntity = new NotifyMessageEntity();
        notifyMessageEntity.setId(dto.getId());
        notifyMessageEntity.setMessage(dto.getMessage());
        notifyMessageEntity.setCreate(dto.getCreate());
        return notifyMessageEntity;
    }

    @Override
    public NotifyMessageDto convertToDto(NotifyMessageEntity model) {
        if(model == null) {
            return null;
        }

        NotifyMessageDto notifyMessageDto = new NotifyMessageDto();
        notifyMessageDto.setId(model.getId());
        notifyMessageDto.setMessage(model.getMessage());
        notifyMessageDto.setCreate(model.getCreate());
        return notifyMessageDto;
    }

    @Override
    public NotifyMessageDto updateData(NotifyMessageDto oldDto, NotifyMessageEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setMessage(newModel.getMessage());
        oldDto.setCreate(newModel.getCreate());
        return oldDto;
    }
}

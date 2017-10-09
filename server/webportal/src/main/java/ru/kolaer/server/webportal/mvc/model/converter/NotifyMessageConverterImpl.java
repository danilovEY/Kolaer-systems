package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageEntity;

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
        return notifyMessageEntity;
    }

    @Override
    public NotifyMessageDto convertToDto(NotifyMessageEntity model) {
        NotifyMessageDto notifyMessageDto = new NotifyMessageDto();
        notifyMessageDto.setId(model.getId());
        notifyMessageDto.setMessage(model.getMessage());
        return notifyMessageDto;
    }

    @Override
    public NotifyMessageDto updateData(NotifyMessageDto oldDto, NotifyMessageEntity newModel) {
        oldDto.setId(newModel.getId());
        oldDto.setMessage(newModel.getMessage());
        return oldDto;
    }
}

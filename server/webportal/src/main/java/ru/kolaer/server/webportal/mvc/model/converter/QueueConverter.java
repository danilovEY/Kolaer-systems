package ru.kolaer.server.webportal.mvc.model.converter;

import ru.kolaer.server.webportal.mvc.model.dto.QueueRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.QueueTargetDto;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.BaseConverter;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface QueueConverter extends BaseConverter<QueueTargetDto, QueueTargetEntity> {

    QueueRequestEntity convertToModel(QueueRequestDto queueRequestDto);

    QueueRequestDto convertToDto(QueueRequestEntity queueRequestEntity);

    List<QueueRequestDto> convertToRequestDto(List<QueueRequestEntity> queueRequestEntities);
}

package ru.kolaer.server.service.queue.converter;

import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.service.queue.dto.QueueRequestDto;
import ru.kolaer.server.service.queue.entity.QueueRequestEntity;
import ru.kolaer.server.service.queue.dto.QueueTargetDto;
import ru.kolaer.server.service.queue.entity.QueueTargetEntity;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface QueueConverter extends BaseConverter<QueueTargetDto, QueueTargetEntity> {

    QueueRequestEntity convertToModel(QueueRequestDto queueRequestDto);

    QueueRequestDto convertToDto(QueueRequestEntity queueRequestEntity);

    List<QueueRequestDto> convertToRequestDto(List<QueueRequestEntity> queueRequestEntities);
}

package ru.kolaer.server.queue.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.queue.model.entity.QueueRequestEntity;
import ru.kolaer.server.queue.model.entity.QueueTargetEntity;
import ru.kolaer.server.webportal.model.dto.queue.QueueRequestDto;
import ru.kolaer.server.webportal.model.dto.queue.QueueTargetDto;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface QueueConverter extends BaseConverter<QueueTargetDto, QueueTargetEntity> {

    QueueRequestEntity convertToModel(QueueRequestDto queueRequestDto);

    QueueRequestDto convertToDto(QueueRequestEntity queueRequestEntity);

    List<QueueRequestDto> convertToRequestDto(List<QueueRequestEntity> queueRequestEntities);
}

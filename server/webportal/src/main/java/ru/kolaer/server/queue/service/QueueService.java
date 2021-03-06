package ru.kolaer.server.queue.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.core.model.dto.queue.PageQueueRequest;
import ru.kolaer.server.core.model.dto.queue.QueueRequestDto;
import ru.kolaer.server.core.model.dto.queue.QueueScheduleDto;
import ru.kolaer.server.core.model.dto.queue.QueueTargetDto;
import ru.kolaer.server.core.service.DefaultService;

public interface QueueService extends DefaultService<QueueTargetDto> {

    QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto);

    QueueRequestDto updateQueueRequest(Long targetId, Long requestId, QueueRequestDto queueRequestDto);

    void deleteQueueRequest(Long targetId, Long requestId);

    PageDto<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize);

    QueueTargetDto update(Long targetId, QueueTargetDto queueTargetDto);

    PageDto<QueueScheduleDto> getSchedulers(PageQueueRequest request);
}

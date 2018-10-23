package ru.kolaer.server.service.queue.service;

import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.service.queue.dto.PageQueueRequest;
import ru.kolaer.server.service.queue.dto.QueueRequestDto;
import ru.kolaer.server.service.queue.dto.QueueScheduleDto;
import ru.kolaer.server.service.queue.dto.QueueTargetDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;

public interface QueueService extends DefaultService<QueueTargetDto> {

    QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto);

    QueueRequestDto updateQueueRequest(Long targetId, Long requestId, QueueRequestDto queueRequestDto);

    void deleteQueueRequest(Long targetId, Long requestId);

    Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize);

    QueueTargetDto update(Long targetId, QueueTargetDto queueTargetDto);

    Page<QueueScheduleDto> getSchedulers(PageQueueRequest request);
}

package ru.kolaer.server.webportal.common.servirces;

import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.microservice.queue.PageQueueRequest;
import ru.kolaer.server.webportal.microservice.queue.QueueRequestDto;
import ru.kolaer.server.webportal.microservice.queue.QueueScheduleDto;
import ru.kolaer.server.webportal.microservice.queue.QueueTargetDto;

public interface QueueService extends DefaultService<QueueTargetDto> {

    QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto);

    QueueRequestDto updateQueueRequest(Long targetId, Long requestId, QueueRequestDto queueRequestDto);

    void deleteQueueRequest(Long targetId, Long requestId);

    Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize);

    QueueTargetDto update(Long targetId, QueueTargetDto queueTargetDto);

    Page<QueueScheduleDto> getSchedulers(PageQueueRequest request);
}

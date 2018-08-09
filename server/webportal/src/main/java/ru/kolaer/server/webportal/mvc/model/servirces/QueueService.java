package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.PageQueueRequest;
import ru.kolaer.server.webportal.mvc.model.dto.QueueRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.QueueScheduleDto;
import ru.kolaer.server.webportal.mvc.model.dto.QueueTargetDto;

public interface QueueService extends DefaultService<QueueTargetDto> {

    QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto);

    QueueRequestDto updateQueueRequest(Long targetId, Long requestId, QueueRequestDto queueRequestDto);

    void deleteQueueRequest(Long targetId, Long requestId);

    Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize);

    QueueTargetDto update(Long targetId, QueueTargetDto queueTargetDto);

    Page<QueueScheduleDto> getSchedulers(PageQueueRequest request);
}

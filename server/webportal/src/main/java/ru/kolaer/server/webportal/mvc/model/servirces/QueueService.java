package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.QueueRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.QueueTargetDto;

public interface QueueService extends DefaultService<QueueTargetDto> {

    @Transactional
    QueueRequestDto addQueueRequest(Long targetId, QueueRequestDto queueRequestDto);

    @Transactional
    void deleteQueueRequest(Long requestId);

    @Transactional
    Page<QueueRequestDto> getAllQueueRequestByTarget(Long targetId, Integer number, Integer pageSize);
}

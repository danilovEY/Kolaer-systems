package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.dto.queue.PageQueueRequest;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueDao extends DefaultDao<QueueTargetEntity> {
    QueueRequestEntity addRequest(QueueRequestEntity queueRequestEntity);

    QueueRequestEntity updateRequest(QueueRequestEntity queueRequestEntity);

    void deleteRequestById(Long requestId);

    List<QueueRequestEntity> findRequestById(Long targetId, LocalDateTime now, Integer number, Integer pageSize);

    Long findCountRequestByTargetId(Long targetId, LocalDateTime now);

    void deleteRequestByIdAndTarget(Long targetId, Long requestId);

    QueueRequestEntity findRequestById(Long queueRequestId);

    QueueRequestEntity findRequestByTargetIdAndId(Long targetId, Long requestId);

    void deleteRequestsByTargetId(Long id);

    Long findCountLastRequests(PageQueueRequest request);

    List<QueueRequestEntity> findLastRequests(PageQueueRequest request);
}

package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.dto.PageQueueRequest;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;

import java.util.List;

public interface QueueDao extends DefaultDao<QueueTargetEntity> {
    QueueRequestEntity addRequest(QueueRequestEntity queueRequestEntity);

    QueueRequestEntity updateRequest(QueueRequestEntity queueRequestEntity);

    void deleteRequestById(Long requestId);

    List<QueueRequestEntity> findRequestById(Long targetId, Integer number, Integer pageSize);

    Long findCountRequestByTargetId(Long targetId);

    void deleteRequestByIdAndTarget(Long targetId, Long requestId);

    QueueRequestEntity findRequestById(Long queueRequestId);

    QueueRequestEntity findRequestByTargetIdAndId(Long targetId, Long requestId);

    void deleteRequestsByTargetId(Long id);

    Long findCountLastRequests(PageQueueRequest request);

    List<QueueRequestEntity> findLastRequests(PageQueueRequest request);
}

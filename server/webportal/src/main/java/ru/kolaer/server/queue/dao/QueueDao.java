package ru.kolaer.server.queue.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.core.model.dto.queue.PageQueueRequest;
import ru.kolaer.server.queue.model.entity.QueueRequestEntity;
import ru.kolaer.server.queue.model.entity.QueueTargetEntity;

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

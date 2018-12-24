package ru.kolaer.server.webportal.model.dto.queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.dto.PaginationRequest;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueueRequest extends PaginationRequest {
    private String name;
    private LocalDateTime afterFrom;

    private QueueSortType sort = QueueSortType.REQUEST_FROM_ASC;
}

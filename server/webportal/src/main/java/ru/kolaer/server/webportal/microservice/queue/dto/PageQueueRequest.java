package ru.kolaer.server.webportal.microservice.queue.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueueRequest extends PaginationRequest {
    private String name;
    private LocalDateTime afterFrom;

    private QueueSortType sort = QueueSortType.REQUEST_FROM_ASC;
}

package ru.kolaer.server.webportal.mvc.model.dto.queue;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

import java.time.LocalDateTime;

@Data
public class PageQueueRequest extends PaginationRequest {
    private String name;
    private LocalDateTime afterFrom;

    private QueueSortType sort = QueueSortType.REQUEST_FROM_ASC;
}

package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageQueueRequest extends PaginationRequest {
    private String name;
    private LocalDateTime afterFrom;

    private QueueSortType sort = QueueSortType.REQUEST_FROM_ASC;
}

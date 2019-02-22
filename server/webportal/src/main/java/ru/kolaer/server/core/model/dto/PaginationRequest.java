package ru.kolaer.server.core.model.dto;

import lombok.Data;

@Data
public class PaginationRequest {
    private int pageNum = 1;
    private int pageSize = 15;
}

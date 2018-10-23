package ru.kolaer.server.core.dto;

import lombok.Data;

@Data
public class PaginationRequest {
    private int number = 1;
    private int pageSize = 15;
}

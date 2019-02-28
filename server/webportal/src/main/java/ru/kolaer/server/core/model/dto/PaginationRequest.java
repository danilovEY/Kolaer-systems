package ru.kolaer.server.core.model.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
public class PaginationRequest {
    private int pageNum = 1;
    private int pageSize = 15;

    public PageRequest toPageRequest() {
        return toPageRequest(Sort.Direction.ASC, "id");
    }

    public PageRequest toPageRequest(Sort.Direction direction, String... properties) {
        return PageRequest.of(pageNum - 1, pageSize, direction, properties);
    }
}

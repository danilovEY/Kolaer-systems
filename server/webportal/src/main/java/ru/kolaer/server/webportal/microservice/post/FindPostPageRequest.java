package ru.kolaer.server.webportal.microservice.post;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindPostPageRequest extends PaginationRequest {
    private String query;
    private Set<Long> departmentIds;
    private boolean onOnePage;
}

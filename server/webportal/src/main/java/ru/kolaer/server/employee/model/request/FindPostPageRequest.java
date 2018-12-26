package ru.kolaer.server.employee.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindPostPageRequest extends PaginationRequest {
    private String query;
    private Set<Long> departmentIds;
    private boolean onOnePage;
}

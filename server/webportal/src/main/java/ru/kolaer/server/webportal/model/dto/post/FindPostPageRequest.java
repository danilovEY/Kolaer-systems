package ru.kolaer.server.webportal.model.dto.post;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.model.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindPostPageRequest extends PaginationRequest {
    private String query;
    private Set<Long> departmentIds;
    private boolean onOnePage;
}

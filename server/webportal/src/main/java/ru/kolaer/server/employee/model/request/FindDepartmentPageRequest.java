package ru.kolaer.server.employee.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDepartmentPageRequest extends PaginationRequest {
    private String query;
    private boolean onOnePage;
}

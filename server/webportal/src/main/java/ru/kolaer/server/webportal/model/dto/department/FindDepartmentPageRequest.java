package ru.kolaer.server.webportal.model.dto.department;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDepartmentPageRequest extends PaginationRequest {
    private String query;
    private boolean onOnePage;
}

package ru.kolaer.server.webportal.microservice.department;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDepartmentPageRequest extends PaginationRequest {
    private String query;
    private boolean onOnePage;
}

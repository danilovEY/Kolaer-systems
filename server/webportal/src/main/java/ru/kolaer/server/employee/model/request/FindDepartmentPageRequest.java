package ru.kolaer.server.employee.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;
import ru.kolaer.server.core.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDepartmentPageRequest extends PaginationRequest {
    private String query;
    private Boolean deleted;

    private Sort.Direction direction = Sort.Direction.ASC;
    private DepartmentSort sort = DepartmentSort.DEFAULT_SORT;
}

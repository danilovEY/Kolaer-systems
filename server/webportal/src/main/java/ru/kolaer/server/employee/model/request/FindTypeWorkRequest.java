package ru.kolaer.server.employee.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindTypeWorkRequest extends PaginationRequest {
    private String searchName;
    private Set<Long> departmentIds;
    private TypeWorkSortType sort = TypeWorkSortType.NAME_ASC;
}

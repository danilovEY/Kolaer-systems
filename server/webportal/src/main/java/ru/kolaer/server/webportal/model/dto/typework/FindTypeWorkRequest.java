package ru.kolaer.server.webportal.model.dto.typework;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindTypeWorkRequest extends PaginationRequest {
    private String searchName;
    private Set<Long> departmentIds;
    private TypeWorkSortType sort = TypeWorkSortType.NAME_ASC;
}

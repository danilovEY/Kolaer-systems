package ru.kolaer.server.service.typework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindTypeWorkRequest extends PaginationRequest {
    private String searchName;
    private Set<Long> departmentIds;
    private TypeWorkSortType sort = TypeWorkSortType.NAME_ASC;
}

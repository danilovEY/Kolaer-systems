package ru.kolaer.server.webportal.mvc.model.dto.typework;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindTypeWorkRequest extends PaginationRequest {
    private String searchName;
    private TypeWorkSortType sort = TypeWorkSortType.NAME_ASC;
}

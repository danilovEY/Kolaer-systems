package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindVacationPageRequest extends PaginationRequest {
    private long employeeId;
    private int year;
    private VacationSortType sort = VacationSortType.DATE_FROM_DESC;
}

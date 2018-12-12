package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.model.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindVacationPeriodPageRequest extends PaginationRequest {
    private VacationPeriodSortType sort = VacationPeriodSortType.YEAR_DESC;
}

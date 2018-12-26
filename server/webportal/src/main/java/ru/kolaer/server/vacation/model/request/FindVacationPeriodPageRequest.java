package ru.kolaer.server.vacation.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindVacationPeriodPageRequest extends PaginationRequest {
    private VacationPeriodSortType sort = VacationPeriodSortType.YEAR_DESC;
}

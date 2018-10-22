package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindVacationPeriodPageRequest extends PaginationRequest {
    private VacationPeriodSortType sort = VacationPeriodSortType.YEAR_DESC;
}

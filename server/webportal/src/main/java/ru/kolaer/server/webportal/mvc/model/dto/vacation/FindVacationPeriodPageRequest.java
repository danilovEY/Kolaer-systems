package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.PaginationRequest;

@Data
public class FindVacationPeriodPageRequest extends PaginationRequest {
    private VacationPeriodSortType sort = VacationPeriodSortType.YEAR_DESC;
}

package ru.kolaer.server.webportal.microservice.vacation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.webportal.common.dto.PaginationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindVacationPageRequest extends PaginationRequest {
    private long employeeId;
    private int year;
    private VacationSortType sort = VacationSortType.DATE_FROM_DESC;
}

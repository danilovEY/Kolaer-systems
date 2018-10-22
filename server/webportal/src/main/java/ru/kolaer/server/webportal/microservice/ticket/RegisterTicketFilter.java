package ru.kolaer.server.webportal.microservice.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.common.dto.EntityFieldName;
import ru.kolaer.server.webportal.common.dto.FilterParam;
import ru.kolaer.server.webportal.common.dto.FilterType;

@Data
public class RegisterTicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    private FilterType typeFilterId = FilterType.EQUAL;
}

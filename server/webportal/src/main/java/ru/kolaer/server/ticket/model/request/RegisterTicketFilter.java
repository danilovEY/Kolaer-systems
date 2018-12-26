package ru.kolaer.server.ticket.model.request;

import lombok.Data;
import ru.kolaer.server.webportal.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.model.dto.FilterParam;
import ru.kolaer.server.webportal.model.dto.FilterType;

@Data
public class RegisterTicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    private FilterType typeFilterId = FilterType.EQUAL;
}

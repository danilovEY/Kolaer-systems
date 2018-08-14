package ru.kolaer.server.webportal.mvc.model.dto.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.dto.EntityFieldName;
import ru.kolaer.server.webportal.mvc.model.dto.FilterParam;
import ru.kolaer.server.webportal.mvc.model.dto.FilterType;

@Data
public class RegisterTicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    private FilterType typeFilterId = FilterType.EQUAL;
}

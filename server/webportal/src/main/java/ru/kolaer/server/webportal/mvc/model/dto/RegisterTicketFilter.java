package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;

@Data
public class RegisterTicketFilter implements FilterParam {
    @EntityFieldName(name = "id")
    private Long filterId;
    private FilterType typeFilterId = FilterType.EQUAL;
}

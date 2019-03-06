package ru.kolaer.server.businesstrip.model.dto.request;

import lombok.Data;
import ru.kolaer.server.core.model.dto.PaginationRequest;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.Set;

@Data
public class FindBusinessTripRequest extends PaginationRequest {
    private Set<@Min(1) Long> businessTripIds = Collections.emptySet();
}

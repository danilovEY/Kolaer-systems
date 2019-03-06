package ru.kolaer.server.contact.model.request;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import ru.kolaer.server.contact.model.entity.ContactType;
import ru.kolaer.server.core.model.dto.PaginationRequest;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindContactPageRequest extends PaginationRequest {
    @NotNull
    private Set<Long> placementIds = Collections.emptySet();

    @NotNull
    private Set<Long> employeeIds = Collections.emptySet();

    @Nullable
    private ContactType type;

    private String query;
}

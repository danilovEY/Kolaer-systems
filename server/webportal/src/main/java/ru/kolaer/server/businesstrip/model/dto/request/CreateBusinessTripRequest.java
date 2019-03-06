package ru.kolaer.server.businesstrip.model.dto.request;

import lombok.Data;
import ru.kolaer.server.businesstrip.model.BusinessTripType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateBusinessTripRequest {
    @NotNull
    private BusinessTripType businessTripType;

    @NotEmpty
    private String organizationName;

    @NotEmpty
    private String documentNumber;

    @NotEmpty
    private String okpoCode;

    @NotNull
    private LocalDate documentDate;

    @Min(1)
    private Long chiefEmployeeId;

    private String comment;

    private String reasonDescription;
    private String reasonDocumentNumber;
    private LocalDate reasonDocumentDate;

}

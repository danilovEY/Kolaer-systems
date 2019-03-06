package ru.kolaer.server.businesstrip.model.dto.request;

import lombok.Data;
import ru.kolaer.server.businesstrip.model.BusinessTripType;

import java.time.LocalDate;

@Data
public class EditBusinessTripRequest {
    private BusinessTripType businessTripType;
    private String organizationName;
    private String documentNumber;
    private String okpoCode;
    private LocalDate documentDate;
    private Long chiefEmployeeId;
    private String comment;
    private String reasonDescription;
    private String reasonDocumentNumber;
    private LocalDate reasonDocumentDate;

}

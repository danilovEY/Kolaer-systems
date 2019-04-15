package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.common.dto.DefaultDto;
import ru.kolaer.server.businesstrip.model.BusinessTripType;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessTripDetailDto extends DefaultDto {

    private BusinessTripType businessTripType;
    private String organizationName;
    private String documentNumber;
    private LocalDate documentDate;
    private String comment;
    private String okpoCode;

    private String reasonDescription;
    private String reasonDocumentNumber;
    private LocalDate reasonDocumentDate;

    private BusinessTripEmployeeInfo chiefEmployee;
    private BusinessTripEmployeeInfo writerEmployee;

}

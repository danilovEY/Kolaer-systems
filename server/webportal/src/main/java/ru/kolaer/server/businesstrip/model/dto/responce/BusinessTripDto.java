package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.common.dto.DefaultDto;
import ru.kolaer.server.businesstrip.model.BusinessTripType;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessTripDto extends DefaultDto {

    private BusinessTripType businessTripType;
    private String organizationName;
    private String documentNumber;
    private LocalDate documentDate;
    private String comment;
    private BusinessTripEmployeeInfo writerEmployee;
    private List<BusinessTripEmployeeInfo> employees;
    private List<BusinessTripDestinationDto> destinations;

}

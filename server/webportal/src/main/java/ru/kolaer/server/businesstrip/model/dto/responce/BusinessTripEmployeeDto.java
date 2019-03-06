package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessTripEmployeeDto extends DefaultDto {

    private long businessTripId;
    private BusinessTripEmployeeInfo employee;
    private String destinationCountry;
    private String destinationCity;
    private String destinationOrganizationName;
    private LocalDate businessTripFrom;
    private LocalDate businessTripTo;
    private int businessTripDays;
    private String targetDescription;
    private String sourceOfFinancing;

}

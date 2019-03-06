package ru.kolaer.server.businesstrip.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class AddEmployeeToBusinessTripRequest {

    @Min(1)
    private Long employeeId;

    @NotEmpty
    private String destinationCity;

    @NotEmpty
    private String destinationCountry;

    @NotEmpty
    private String destinationOrganizationName;

    @NotNull
    private LocalDate businessTripFrom;

    @NotNull
    private LocalDate businessTripTo;

    private String targetDescription;
    private String sourceOfFinancing;

}

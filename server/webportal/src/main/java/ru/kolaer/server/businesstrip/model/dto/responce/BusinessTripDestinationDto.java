package ru.kolaer.server.businesstrip.model.dto.responce;

import lombok.Data;

@Data
public class BusinessTripDestinationDto {
    private String destinationCountry;
    private String destinationCity;
    private String destinationOrganizationName;
}

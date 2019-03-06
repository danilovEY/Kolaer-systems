package ru.kolaer.server.businesstrip.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_trip_employee")
@Data
public class BusinessTripEmployeeEntity extends DefaultEntity {

    @Column(name = "business_trip_id", nullable = false)
    private long businessTripId;

    @Column(name = "employee_id", nullable = false)
    private long employeeId;

    @Column(name = "destination_country", nullable = false, length = 50)
    private String destinationCountry;

    @Column(name = "destination_city", nullable = false, length = 50)
    private String destinationCity;

    @Column(name = "destination_organization_name", nullable = false)
    private String destinationOrganizationName;

    @Column(name = "business_trip_from", nullable = false)
    private LocalDate businessTripFrom;

    @Column(name = "business_trip_to", nullable = false)
    private LocalDate businessTripTo;

    @Column(name = "business_trip_days", nullable = false)
    private int businessTripDays;

    @Column(name = "target_description", nullable = false)
    private String targetDescription;

    @Column(name = "source_of_financing", nullable = false)
    private String sourceOfFinancing;

}

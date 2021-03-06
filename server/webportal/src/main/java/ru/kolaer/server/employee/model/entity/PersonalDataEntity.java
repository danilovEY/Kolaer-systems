package ru.kolaer.server.employee.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personal_data")
@Data
public class PersonalDataEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "marital_status", nullable = false)
    private String maritalStatus;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address_registration", nullable = false)
    private String addressRegistration;

    @Column(name = "address_residential", nullable = false)
    private String addressResidential;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "disability_group", length = 50)
    private String disabilityGroup;

    @Column(name = "disability_date", length = 50)
    private LocalDate disabilityDate;
}

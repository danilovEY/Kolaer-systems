package ru.kolaer.server.webportal.model.entity.education;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
public class EducationEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "type_education", length = 100, nullable = false)
    private String typeEducation;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "qualification", nullable = false)
    private String qualification;

    @Column(name = "document", length = 50, nullable = false)
    private String document;

    @Column(name = "document_number", length = 20, nullable = false)
    private String document_number;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expiration_date;
}

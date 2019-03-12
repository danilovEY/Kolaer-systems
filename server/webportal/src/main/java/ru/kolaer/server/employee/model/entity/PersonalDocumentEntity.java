package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "personal_document")
@Getter
@Setter
public class PersonalDocumentEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_issue")
    private LocalDate dateOfIssue;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "issued_by")
    private String issuedBy;
}

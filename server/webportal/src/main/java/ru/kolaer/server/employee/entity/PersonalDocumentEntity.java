package ru.kolaer.server.employee.entity;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "personal_document")
@Data
public class PersonalDocumentEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_issue", nullable = false)
    private LocalDate dateOfIssue;

    @Column(name = "document_number", length = 50, nullable = false)
    private String documentNumber;

    @Column(name = "issued_by")
    private String issuedBy;
}

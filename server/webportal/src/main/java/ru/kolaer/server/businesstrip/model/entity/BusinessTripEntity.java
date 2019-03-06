package ru.kolaer.server.businesstrip.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.businesstrip.model.BusinessTripType;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_trip")
@Data
public class BusinessTripEntity extends DefaultEntity {

    @Column(name = "business_trip_type", nullable = false)
    private BusinessTripType businessTripType;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "okpo_code", nullable = false, length = 10)
    private String okpoCode;

    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @Column(name = "reason_description", nullable = false)
    private String reasonDescription;

    @Column(name = "reason_document_number", nullable = false, length = 50)
    private String reasonDocumentNumber;

    @Column(name = "reason_document_date", nullable = false)
    private LocalDate reasonDocumentDate;

    @Column(name = "chief_employee_id", nullable = false)
    private Long chiefEmployeeId;

    @Column(name = "writer_employee_id", nullable = false)
    private Long writerEmployeeId;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "businessTripId")
    private List<BusinessTripEmployeeEntity> employees;

}

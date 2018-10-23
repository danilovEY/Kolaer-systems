package ru.kolaer.server.webportal.microservice.department.entity;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "department")
@Data
public class DepartmentEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "abbreviated_name")
    private String abbreviatedName;

    @Column(name = "chief_employee_id")
    private Long chiefEmployeeId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "code")
    private int code;

}

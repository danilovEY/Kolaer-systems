package ru.kolaer.server.employee.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by danilovey on 12.09.2016.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "department")
@Data
public class DepartmentEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "abbreviated_name")
    private String abbreviatedName;

    @Column(name = "chief_employee_id")
    private Long chiefEmployeeId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "code")
    private int code;

}

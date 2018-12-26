package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "military_registration")
@Getter
@Setter
public class MilitaryRegistrationEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "rank", nullable = false)
    private String rank;

    @Column(name = "status_by", nullable = false)
    private String statusBy;

}

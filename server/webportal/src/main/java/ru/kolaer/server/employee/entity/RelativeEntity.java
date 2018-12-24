package ru.kolaer.server.employee.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "relative")
@Getter
@Setter
public class RelativeEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "initials")
    private String initials;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}

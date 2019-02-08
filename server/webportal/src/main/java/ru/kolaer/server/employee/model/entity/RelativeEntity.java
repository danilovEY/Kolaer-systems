package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "relative")
@Getter
@Setter
public class RelativeEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "initials")
    private String initials;

    @Column(name = "relation_degree", length = 50)
    private String relationDegree;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}

package ru.kolaer.server.webportal.model.entity.relative;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "relative")
@Data
public class RelativeEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "initials")
    private String initials;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}

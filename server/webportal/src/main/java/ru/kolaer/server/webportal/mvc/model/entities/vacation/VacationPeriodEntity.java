package ru.kolaer.server.webportal.mvc.model.entities.vacation;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "vacation_period")
@Data
public class VacationPeriodEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "status", nullable = false)
    private VacationPeriodStatus status;

}

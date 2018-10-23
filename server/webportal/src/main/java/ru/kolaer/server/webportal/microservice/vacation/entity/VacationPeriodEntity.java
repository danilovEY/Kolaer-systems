package ru.kolaer.server.webportal.microservice.vacation.entity;

import lombok.Data;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.webportal.microservice.vacation.VacationPeriodStatus;

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

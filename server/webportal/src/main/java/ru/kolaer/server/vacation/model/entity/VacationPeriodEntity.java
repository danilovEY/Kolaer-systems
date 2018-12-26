package ru.kolaer.server.vacation.model.entity;

import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vacation_period")
@Data
public class VacationPeriodEntity extends DefaultEntity {

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "status", nullable = false)
    private VacationPeriodStatus status;

}

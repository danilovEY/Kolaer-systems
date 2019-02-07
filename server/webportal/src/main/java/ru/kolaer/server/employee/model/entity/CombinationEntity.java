package ru.kolaer.server.employee.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "combination")
@Data
public class CombinationEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "post", nullable = false)
    private String post;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}

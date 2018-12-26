package ru.kolaer.server.employee.model.entity;

import lombok.Data;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "punishment")
@Data
public class PunishmentEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "order_number", length = 100, nullable = false)
    private String orderNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "start_punishment_date", nullable = false)
    private LocalDate startPunishmentDate;

}

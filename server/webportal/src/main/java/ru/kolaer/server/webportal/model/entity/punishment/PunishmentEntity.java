package ru.kolaer.server.webportal.model.entity.punishment;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "punishment")
@Data
public class PunishmentEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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

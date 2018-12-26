package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "achievement")
@Getter
@Setter
public class AchievementEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "order_number", length = 20, nullable = false)
    private String orderNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
}

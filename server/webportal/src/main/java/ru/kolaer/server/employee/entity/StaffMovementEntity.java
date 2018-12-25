package ru.kolaer.server.employee.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staff_movement")
@Getter
@Setter
public class StaffMovementEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "post")
    private String post;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "category_unit", length = 50, nullable = false)
    private String categoryUnit;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Column(name = "surcharge_harmfulness")
    private Integer surchargeHarmfulness;

    @Column(name = "card_slam", length = 50)
    private String cardSlam;

    @Column(name = "subclass_working_conditions", length = 50)
    private String subclassWorkingConditions;

    @Column(name = "order_number", length = 50)
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}

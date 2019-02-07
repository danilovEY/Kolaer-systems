package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staff_movement")
@Getter
@Setter
public class StaffMovementEntity extends DefaultEntity {

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

    @Column(name = "class_working_conditions", length = 50)
    private String classWorkingConditions;

    @Column(name = "sub_class_working_conditions", length = 50)
    private String subClassWorkingConditions;

    @Column(name = "order_number", length = 50)
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "start_work_date", nullable = false)
    private LocalDate startWorkDate;

    @Column(name = "end_work_date")
    private LocalDate endWorkDate;
}

package ru.kolaer.server.vacation.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vacation_balance")
@Data
public class VacationBalanceEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity employee;

    @Column(name = "next_year_balance", nullable = false)
    private int nextYearBalance;

    @Column(name = "current_year_balance", nullable = false)
    private int currentYearBalance;

    @Column(name = "prev_year_balance", nullable = false)
    private int prevYearBalance;
}

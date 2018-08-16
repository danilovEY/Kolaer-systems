package ru.kolaer.server.webportal.mvc.model.entities.vacation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;

@Entity
@Table(name = "vacation_balance")
@Data
public class VacationBalanceEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonIgnore
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity employee;

    @Column(name = "next_year_balance", nullable = false)
    private int nextYearBalance;

    @Column(name = "current_year_balance", nullable = false)
    private int currentYearBalance;

    @Column(name = "prev_year_balance", nullable = false)
    private int prevYearBalance;
}

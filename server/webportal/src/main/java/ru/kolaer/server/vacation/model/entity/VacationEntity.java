package ru.kolaer.server.vacation.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacation")
@Data
public class VacationEntity extends DefaultEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity employee;

    @Column(name = "note")
    private String note;

    @Column(name = "vacation_from", nullable = false)
    private LocalDate vacationFrom;

    @Column(name = "vacation_to", nullable = false)
    private LocalDate vacationTo;

    @Column(name = "vacation_days", nullable = false)
    private int vacationDays;

    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;
}

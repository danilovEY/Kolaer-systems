package ru.kolaer.server.service.vacation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import employee.entity.EmployeeEntity;
import ru.kolaer.server.service.vacation.VacationType;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ru/kolaer/server/service/vacation")
@Data
public class VacationEntity implements BaseEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

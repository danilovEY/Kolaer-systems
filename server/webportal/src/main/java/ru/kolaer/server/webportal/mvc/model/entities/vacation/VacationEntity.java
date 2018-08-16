package ru.kolaer.server.webportal.mvc.model.entities.vacation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacation")
@Data
public class VacationEntity implements BaseEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonIgnore
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity employee;

    @Column(name = "note")
    private String note;

    @Column(name = "vacation_from", nullable = false)
    private LocalDateTime vacationFrom;

    @Column(name = "vacation_to", nullable = false)
    private LocalDateTime vacationTo;

    @Column(name = "days", nullable = false)
    private int days;

    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;
}

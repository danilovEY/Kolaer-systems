package ru.kolaer.server.employee.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.server.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "employment_history")
@Getter
@Setter
public class EmploymentHistoryEntity extends BaseEntity {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "post")
    private String post;

    @Column(name = "start_work_date", nullable = false)
    private LocalDate startWorkDate;

    @Column(name = "end_work_date")
    private LocalDate endWorkDate;

}

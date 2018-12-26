package ru.kolaer.server.ticket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 13.12.2016.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccountEntity extends DefaultEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable=false, updatable=false)
    private EmployeeEntity employee;

    @Column(name = "account_check", nullable = false, length = 16, unique = true)
    private String check;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}

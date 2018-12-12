package ru.kolaer.server.webportal.model.entity.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 13.12.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccountEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

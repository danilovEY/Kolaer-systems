package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

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

    @Column(name = "check", nullable = false, length = 16, unique = true)
    private String check;
}

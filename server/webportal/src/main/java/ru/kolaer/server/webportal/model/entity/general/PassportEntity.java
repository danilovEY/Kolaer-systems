package ru.kolaer.server.webportal.model.entity.general;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 24.01.2017.
 */
@Entity
@Table(name = "passport")
@Data
public class PassportEntity implements Serializable, BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable=false, updatable=false)
    private EmployeeEntity employee;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "serial", length = 4)
    private String serial;

    @Column(name = "number", length = 6)
    private String number;
}

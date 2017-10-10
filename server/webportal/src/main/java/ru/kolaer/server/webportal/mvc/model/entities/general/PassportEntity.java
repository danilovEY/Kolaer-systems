package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 24.01.2017.
 */
@Entity
@Table(name = "passport")
@Data
public class PassportEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

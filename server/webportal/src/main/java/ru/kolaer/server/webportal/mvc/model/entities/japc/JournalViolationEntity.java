package ru.kolaer.server.webportal.mvc.model.entities.japc;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 08.09.2016.
 */
@Entity
@Table(name = "violation_journal")
@Data
public class JournalViolationEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "journalViolation", fetch = FetchType.LAZY)
    private List<ViolationEntity> violations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "department_id")
    private Long departmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_writer", nullable = false)
    private EmployeeEntity writer;
}

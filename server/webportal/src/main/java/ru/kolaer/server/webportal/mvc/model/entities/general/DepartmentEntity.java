package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "department")
@Data
public class DepartmentEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "abbreviated_name")
    private String abbreviatedName;

    @Column(name = "chief_employee_id")
    private Long chiefEmployeeId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "external_id")
    private String externalId;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_employee_id", insertable=false, updatable=false)
    private EmployeeEntity chief;*/

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviatedName='" + abbreviatedName + '\'' +
                ", chiefEmployeeId=" + chiefEmployeeId +
                ", deleted=" + deleted +
                ", externalId='" + externalId + '\'' +
                '}';
    }
}

package ru.kolaer.server.webportal.model.entity.militaryregistration;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "military_registration")
@Data
public class MilitaryRegistrationEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "rank", nullable = false)
    private String rank;

    @Column(name = "status_by", nullable = false)
    private String statusBy;

}

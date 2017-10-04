package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Data
@Table(name = "ticket_register")
public class TicketRegister implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "close", nullable = false)
    private boolean close;

    @Column(name = "department_id")
    private Long departmentId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticketRegister")
    private List<TicketEntity> tickets;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department")
    private DepartmentEntity department;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_register")
    private Date createRegister;

}

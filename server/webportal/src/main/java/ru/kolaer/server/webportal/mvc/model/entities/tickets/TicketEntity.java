package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "ticket")
@Data
public class TicketEntity implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "register_id", nullable = false)
    private Long registerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private TicketRegister ticketRegister;
}

package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "tickets")
@ApiModel(value = "(Талоны) Талон ЛПП")
@Data
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets.seq")
    private Integer id;

    @ApiModelProperty(value = "Сотрудник")
    @OneToOne(targetEntity = EmployeeEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_employee")
    private EmployeeEntity employee;

    @ApiModelProperty(value = "Колличество талонов")
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    private TicketRegister ticketRegister;
}

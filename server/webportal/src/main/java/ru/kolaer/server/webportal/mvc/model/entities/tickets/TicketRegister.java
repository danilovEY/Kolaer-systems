package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntityDecorator;

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
@ApiModel("(Талоны) Реестр талонов")
public class TicketRegister implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "close")
    private boolean close;

    @ApiModelProperty(value = "Список талонов")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketRegister")
    private List<Ticket> tickets;

    @ApiModelProperty(value = "Подразделение")
    @OneToOne(targetEntity = DepartmentEntityDecorator.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_department")
    private DepartmentEntity department;

    @ApiModelProperty(value = "Дата создания")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createRegister;

}

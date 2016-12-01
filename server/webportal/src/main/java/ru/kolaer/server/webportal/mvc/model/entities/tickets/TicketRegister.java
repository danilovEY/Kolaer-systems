package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "ticket_register")
@ApiModel("(Талоны) Реестр талонов")
public class TicketRegister implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "Список талонов")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ticket_register_depatpament", joinColumns = {@JoinColumn(name = "id_register")},
            inverseJoinColumns = { @JoinColumn(name = "id_ticket")})
    private List<Ticket> tickents;

    @ApiModelProperty(value = "Подразделение")
    @OneToOne(targetEntity = GeneralDepartamentEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_departament")
    private GeneralDepartamentEntity departament;

    @ApiModelProperty(value = "Дата создания")
    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "ru", pattern = "dd.MM.yyyy", timezone = "Europe/Moscow")
    @Temporal(TemporalType.DATE)
    private Date createRegister;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Ticket> getTickents() {
        return tickents;
    }

    public void setTickents(List<Ticket> tickents) {
        this.tickents = tickents;
    }

    public GeneralDepartamentEntity getDepartament() {
        return departament;
    }

    public void setDepartament(GeneralDepartamentEntity departament) {
        this.departament = departament;
    }

    public Date getCreateRegister() {
        return createRegister;
    }

    public void setCreateRegister(Date createRegister) {
        this.createRegister = createRegister;
    }
}

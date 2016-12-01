package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "tickets")
@ApiModel(value = "(Талоны) Талон ЛПП")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "Сотрудник")
    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_employee")
    private GeneralEmployeesEntity employee;

    @ApiModelProperty(value = "Колличество талонов")
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GeneralEmployeesEntity getEmployee() {
        return employee;
    }

    public void setEmployee(GeneralEmployeesEntity employee) {
        this.employee = employee;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

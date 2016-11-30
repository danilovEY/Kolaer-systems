package ru.kolaer.server.webportal.mvc.model.entities.tickets;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 30.11.2016.
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "id_employee")
    private GeneralEmployeesEntity employee;

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

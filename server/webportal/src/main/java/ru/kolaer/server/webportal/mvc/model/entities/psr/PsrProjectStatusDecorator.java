package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectStatus;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectStatusBase;

import javax.persistence.*;

/**
 * Created by danilovey on 29.07.2016.
 */
//@Entity
//@Table(name = "psr_project_status")
public class PsrProjectStatusDecorator implements PsrProjectStatus {
    private PsrProjectStatusBase psrProjectStatus;

    public PsrProjectStatusDecorator() {
        this.psrProjectStatus = new PsrProjectStatusBase();
    }


    public int getId() {
        return this.psrProjectStatus.getId();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public void setId(int id) {
        this.psrProjectStatus.setId(id);
    }


    public String getType() {
        return this.psrProjectStatus.getType();
    }

    @Column(name = "type")
    public void setType(String type) {
        this.psrProjectStatus.setType(type);
    }
}

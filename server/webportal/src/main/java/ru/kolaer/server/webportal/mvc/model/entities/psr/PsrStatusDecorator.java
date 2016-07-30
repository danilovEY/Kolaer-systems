package ru.kolaer.server.webportal.mvc.model.entities.psr;

import org.hibernate.annotations.Immutable;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatusBase;

import javax.persistence.*;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_status")
@Immutable
public class PsrStatusDecorator implements PsrStatus {
    private PsrStatus psrProjectStatus;

    public PsrStatusDecorator() {
        this.psrProjectStatus = new PsrStatusBase();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.psrProjectStatus.getId();
    }

    public void setId(int id) {
        this.psrProjectStatus.setId(id);
    }


    @Column(name = "type")
    public String getType() {
        return this.psrProjectStatus.getType();
    }

    public void setType(String type) {
        this.psrProjectStatus.setType(type);
    }
}
package ru.kolaer.server.webportal.mvc.model.entities.psr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStateBase;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_state")
public class PsrStateDecorator implements PsrState {
    private PsrState psrProjectState;

    public PsrStateDecorator() {
        this.psrProjectState = new PsrStateBase();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.psrProjectState.getId();
    }


    public void setId(int id) {
        this.psrProjectState.setId(id);
    }

    @Column(name = "comment")
    public String getComment() {
        return this.psrProjectState.getComment();
    }


    public void setComment(String comment) {
        this.psrProjectState.setComment(comment);
    }

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return this.psrProjectState.getDate();
    }


    public void setDate(Date date) {
        this.psrProjectState.setDate(date);
    }

    @Column(name = "is_plan")
    public boolean isPlan() {
        return this.psrProjectState.isPlan();
    }


    public void setPlan(boolean plan) {
        this.psrProjectState.setPlan(plan);
    }
}

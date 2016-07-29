package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_register")
public class PsrRegisterDecorator implements PsrRegister {
    private PsrRegister psrRegister;

    public PsrRegisterDecorator() {
        this.psrRegister = new PsrRegisterBase();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.psrRegister.getId();
    }


    public void setId(int id) {
        this.psrRegister.setId(id);
    }


    @Transient
    public PsrProjectStatus getStatus() {
        return this.psrRegister.getStatus();
    }


    //@OneToOne(targetEntity = PsrProjectStatusDecorator.class, fetch = FetchType.EAGER, mappedBy = "id_project")
    //@JoinTable(name = "psr_project_status", joinColumns = @JoinColumn(name = "id"))
    public void setStatus(PsrProjectStatus status) {
        this.psrRegister.setStatus(status);
    }

    @Transient
    public GeneralEmployeesEntity getAuthor() {
        return this.psrRegister.getAuthor();
    }

    //@OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.EAGER)
    public void setAuthor(GeneralEmployeesEntity author) {
        this.psrRegister.setAuthor(author);
    }

    @Transient
    public GeneralEmployeesEntity getAdmin() {
        return this.psrRegister.getAdmin();
    }

    //@OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.EAGER, mappedBy = "pnumber")
    public void setAdmin(GeneralEmployeesEntity admin) {
        this.psrRegister.setAdmin(admin);
    }

    @Column(name = "name")
    public String getName() {
        return this.psrRegister.getName();
    }


    public void setName(String name) {
        this.psrRegister.setName(name);
    }

    @Column(name = "date_open")
    @Temporal(TemporalType.DATE)
    public Date getDateOpen() {
        return this.psrRegister.getDateOpen();
    }


    public void setDateOpen(Date dateOpen) {
        this.psrRegister.setDateOpen(dateOpen);
    }

    @Column(name = "date_close")
    @Temporal(TemporalType.DATE)
    public Date getDateClose() {
        return this.psrRegister.getDateClose();
    }

    public void setDateClose(Date dateClose) {
        this.psrRegister.setDateClose(dateClose);
    }

    @Column(name = "comment")
    public String getComment() {
        return this.psrRegister.getComment();
    }

    public void setComment(String comment) {
        this.psrRegister.setComment(comment);
    }

    @OneToMany(targetEntity = PsrAttachmentDecorator.class, fetch = FetchType.EAGER)
    @JoinTable(name = "psr_project_attachment", joinColumns = @JoinColumn(name = "id_project"), inverseJoinColumns = @JoinColumn(name = "id_attachment"))
    public List<PsrAttachment> getAttachments() {
        return this.psrRegister.getAttachments();
    }

    //
    public void setAttachments(List<PsrAttachment> attachments) {
        this.psrRegister.setAttachments(attachments);
    }

    @Transient
    public List<PsrState> getStateList() {
        return this.psrRegister.getStateList();
    }

    //@OneToMany(targetEntity = PsrAttachmentDecorator.class, fetch = FetchType.EAGER, mappedBy = "id_project")
    public void setStateList(List<PsrState> stateList) {
        this.psrRegister.setStateList(stateList);
    }
}

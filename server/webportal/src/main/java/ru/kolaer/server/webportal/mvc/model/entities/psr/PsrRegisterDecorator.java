package ru.kolaer.server.webportal.mvc.model.entities.psr;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsrRegisterDecorator implements PsrRegister {
    private PsrRegister psrRegister;

    public PsrRegisterDecorator() {
        this.psrRegister = new PsrRegisterBase();
    }

    public PsrRegisterDecorator(PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return this.psrRegister.getId();
    }

    public void setId(int id) {
        this.psrRegister.setId(id);
    }


    @OneToOne(targetEntity = PsrStatusDecorator.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false)
    public PsrStatus getStatus() {
        return this.psrRegister.getStatus();
    }

    public void setStatus(PsrStatus status) {
        this.psrRegister.setStatus(status);
    }

    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false)
    public GeneralEmployeesEntity getAuthor() {
        return this.psrRegister.getAuthor();
    }

    public void setAuthor(GeneralEmployeesEntity author) {
        this.psrRegister.setAuthor(author);
    }

    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_project_admin")
    public GeneralEmployeesEntity getAdmin() {
        return this.psrRegister.getAdmin();
    }

    public void setAdmin(GeneralEmployeesEntity admin) {
        this.psrRegister.setAdmin(admin);
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.psrRegister.getName();
    }


    public void setName(String name) {
        this.psrRegister.setName(name);
    }

    @Column(name = "date_open", nullable = false)
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

    @OneToMany(targetEntity = PsrStateDecorator.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_psr_project")
    public List<PsrState> getStateList() {
        return this.psrRegister.getStateList();
    }

    public void setStateList(List<PsrState> stateList) {
        this.psrRegister.setStateList(stateList);
    }


    @OneToMany(targetEntity = PsrAttachmentDecorator.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_psr_project")
    @Fetch(FetchMode.SELECT)
    public List<PsrAttachment> getAttachments() {
        return this.psrRegister.getAttachments();
    }

    public void setAttachments(List<PsrAttachment> attachments) {
        this.psrRegister.setAttachments(attachments);
    }


}

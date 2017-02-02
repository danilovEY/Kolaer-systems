package ru.kolaer.server.webportal.mvc.model.entities.psr;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public PsrRegisterDecorator(PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
        //this.setStateList(psrRegister.getStateList().stream().map(PsrStateDecorator::new).collect(Collectors.toList()));
        //this.setAuthor(new GeneralEmployeesEntityDecorator(psrRegister.getAuthor()));
        //this.setStatus(new PsrStatusDecorator(psrRegister.getStatus()));
        //this.setStateList(psrRegister.getStateList().stream().map(PsrStateDecorator::new).collect(Collectors.toList()));
    }

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "psr_register.seq", sequenceName = "psr_register_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "psr_register.seq")
    public Integer getId() {
        return this.psrRegister.getId();
    }

    public void setId(Integer id) {
        this.psrRegister.setId(id);
    }


    @OneToOne(targetEntity = PsrStatusDecorator.class, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_status")
    public PsrStatus getStatus() {
        return this.psrRegister.getStatus();
    }

    public void setStatus(PsrStatus status) {
        this.psrRegister.setStatus(status);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_author")
    @NotFound(action = NotFoundAction.IGNORE)
    public EmployeeEntity getAuthor() {
        return this.psrRegister.getAuthor();
    }

    public void setAuthor(EmployeeEntity author) {
        this.psrRegister.setAuthor(author);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, optional = false, cascade = CascadeType.MERGE)
    public EmployeeEntity getAdmin() {
        return this.psrRegister.getAdmin();
    }

    public void setAdmin(EmployeeEntity admin) {
        this.psrRegister.setAdmin(admin);
    }

    @Column(name = "name")
    public String getName() {
        return this.psrRegister.getName();
    }

    public void setName(String name) {
        this.psrRegister.setName(name);
    }


    @Column(name = "date_open", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateOpen() {
        return this.psrRegister.getDateOpen();
    }

    public void setDateOpen(Date dateOpen) {
        this.psrRegister.setDateOpen(dateOpen);
    }


    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateClose() {
        return this.psrRegister.getDateClose();
    }

    public void setDateClose(Date dateClose) {
        this.psrRegister.setDateClose(dateClose);
    }


    @Column(name = "comment", length = 1000)
    public String getComment() {
        return this.psrRegister.getComment();
    }

    public void setComment(String comment) {
        this.psrRegister.setComment(comment);
    }


    @OneToMany(targetEntity = PsrStateDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_psr_project")
    public List<PsrState> getStateList() {
        return this.psrRegister.getStateList();
    }

    public void setStateList(List<PsrState> stateList) {
        this.psrRegister.setStateList(stateList);
    }


    @OneToMany(targetEntity = PsrAttachmentDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_psr_project")
    @Fetch(FetchMode.SELECT)
    public List<PsrAttachment> getAttachments() {
        return this.psrRegister.getAttachments();
    }

    public void setAttachments(List<PsrAttachment> attachments) {
        this.psrRegister.setAttachments(attachments);
    }


}

package ru.kolaer.server.webportal.mvc.model.entities.psr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.psr.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_register")
@ApiModel(value="ПРС-проект", description="Структура ПСР-проекта.", subTypes = PsrRegister.class)
public class PsrRegisterDecorator implements PsrRegister {
    private PsrRegister psrRegister;

    public PsrRegisterDecorator() {
        this.psrRegister = new PsrRegisterBase();
    }

    public PsrRegisterDecorator(PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
        this.setStateList(psrRegister.getStateList().stream().map(PsrStateDecorator::new).collect(Collectors.toList()));
        this.setAuthor(new GeneralEmployeesEntityDecorator(psrRegister.getAuthor()));
        this.setStatus(new PsrStatusDecorator(psrRegister.getStatus()));
        this.setStateList(psrRegister.getStateList().stream().map(PsrStateDecorator::new).collect(Collectors.toList()));
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID проекта")
    public Integer getId() {
        return this.psrRegister.getId();
    }

    public void setId(Integer id) {
        this.psrRegister.setId(id);
    }


    @ApiModelProperty(value = "Статус проекта")
    @OneToOne(targetEntity = PsrStatusDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_status", nullable = true)
    public PsrStatus getStatus() {
        return this.psrRegister.getStatus();
    }

    public void setStatus(PsrStatus status) {
        this.psrRegister.setStatus(status);
    }

    @ApiModelProperty(value = "Автор проекта")
    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_author", nullable = true)
    public GeneralEmployeesEntity getAuthor() {
        return this.psrRegister.getAuthor();
    }

    public void setAuthor(GeneralEmployeesEntity author) {
        this.psrRegister.setAuthor(author);
    }

    @ApiModelProperty(value = "Куратор проекта")
    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_project_admin")
    public GeneralEmployeesEntity getAdmin() {
        return this.psrRegister.getAdmin();
    }

    public void setAdmin(GeneralEmployeesEntity admin) {
        this.psrRegister.setAdmin(admin);
    }

    @ApiModelProperty(value = "Имя проекта")
    @Column(name = "name", nullable = true)
    public String getName() {
        return this.psrRegister.getName();
    }


    public void setName(String name) {
        this.psrRegister.setName(name);
    }

    @ApiModelProperty(value = "Дата открытия проекта")
    @Column(name = "date_open", nullable = true)
    @Temporal(TemporalType.DATE)
    public Date getDateOpen() {
        return this.psrRegister.getDateOpen();
    }


    public void setDateOpen(Date dateOpen) {
        this.psrRegister.setDateOpen(dateOpen);
    }

    @ApiModelProperty(value = "Дата завершения проекта")
    @Column(name = "date_close")
    @Temporal(TemporalType.DATE)
    public Date getDateClose() {
        return this.psrRegister.getDateClose();
    }

    public void setDateClose(Date dateClose) {
        this.psrRegister.setDateClose(dateClose);
    }

    @ApiModelProperty(value = "Описание проекта")
    @Column(name = "comment")
    public String getComment() {
        return this.psrRegister.getComment();
    }

    public void setComment(String comment) {
        this.psrRegister.setComment(comment);
    }

    @ApiModelProperty(value = "Лог проекта")
    @OneToMany(targetEntity = PsrStateDecorator.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_psr_project")
    public List<PsrState> getStateList() {
        return this.psrRegister.getStateList();
    }

    public void setStateList(List<PsrState> stateList) {
        this.psrRegister.setStateList(stateList);
    }

    @ApiModelProperty(value = "Файлы проекта")
    @OneToMany(targetEntity = PsrAttachmentDecorator.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_psr_project")
    @Fetch(FetchMode.SELECT)
    public List<PsrAttachment> getAttachments() {
        return this.psrRegister.getAttachments();
    }

    public void setAttachments(List<PsrAttachment> attachments) {
        this.psrRegister.setAttachments(attachments);
    }


}

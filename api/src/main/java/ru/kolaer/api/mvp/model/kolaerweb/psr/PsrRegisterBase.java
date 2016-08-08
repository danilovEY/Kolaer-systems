package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrRegisterBase implements PsrRegister {
    private Integer id = null;
    private PsrStatus status;
    private GeneralEmployeesEntity author;
    private GeneralEmployeesEntity admin;
    private String name;
    private Date dateOpen;
    private Date dateClose;
    private String comment;
    private List<PsrAttachment> attachments;
    private List<PsrState> stateList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PsrStatus getStatus() {
        return status;
    }

    public void setStatus(PsrStatus status) {
        this.status = status;
    }

    public GeneralEmployeesEntity getAuthor() {
        return author;
    }

    public void setAuthor(GeneralEmployeesEntity autor) {
        this.author = autor;
    }

    public GeneralEmployeesEntity getAdmin() {
        return admin;
    }

    public void setAdmin(GeneralEmployeesEntity admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Date dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PsrAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PsrAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<PsrState> getStateList() {
        return stateList;
    }

    public void setStateList(List<PsrState> stateList) {
        this.stateList = stateList;
    }
}

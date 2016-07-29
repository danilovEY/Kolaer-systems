package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;

import java.sql.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrRegisterBase implements PsrRegister {
    private int id;
    private PsrProjectStatusBase status;
    private GeneralEmployeesEntityBase author;
    private GeneralEmployeesEntityBase admin;
    private String name;
    private Date dateOpen;
    private Date dateClose;
    private String comment;
    private List<PsrProjectAttachmentBase> attachments;
    private List<PsrProjectStateBase> stateList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PsrProjectStatusBase getStatus() {
        return status;
    }

    public void setStatus(PsrProjectStatusBase status) {
        this.status = status;
    }

    public GeneralEmployeesEntityBase getAuthor() {
        return author;
    }

    public void setAuthor(GeneralEmployeesEntityBase autor) {
        this.author = autor;
    }

    public GeneralEmployeesEntityBase getAdmin() {
        return admin;
    }

    public void setAdmin(GeneralEmployeesEntityBase admin) {
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

    public List<PsrProjectAttachmentBase> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PsrProjectAttachmentBase> attachments) {
        this.attachments = attachments;
    }

    public List<PsrProjectStateBase> getStateList() {
        return stateList;
    }

    public void setStateList(List<PsrProjectStateBase> stateList) {
        this.stateList = stateList;
    }
}

package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.sql.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrRegister {
    private int id;
    private PsrProjectStatus status;
    private GeneralEmployeesEntity autor;
    private GeneralEmployeesEntity admin;
    private String name;
    private Date dateOpen;
    private Date dateClose;
    private String comment;
    private List<PsrProjectAttachment> attachments;
    private List<PsrProjectState> stateList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PsrProjectStatus getStatus() {
        return status;
    }

    public void setStatus(PsrProjectStatus status) {
        this.status = status;
    }

    public GeneralEmployeesEntity getAutor() {
        return autor;
    }

    public void setAutor(GeneralEmployeesEntity autor) {
        this.autor = autor;
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

    public List<PsrProjectAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PsrProjectAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<PsrProjectState> getStateList() {
        return stateList;
    }

    public void setStateList(List<PsrProjectState> stateList) {
        this.stateList = stateList;
    }
}

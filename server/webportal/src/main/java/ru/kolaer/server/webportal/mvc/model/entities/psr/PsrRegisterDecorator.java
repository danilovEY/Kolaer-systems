package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.psr.*;

import java.sql.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrRegisterDecorator implements PsrRegister {
    private PsrRegisterBase psrRegister;

    public PsrRegisterDecorator() {
        this.psrRegister = new PsrRegisterBase();
    }

    public int getId() {
        return this.psrRegister.getId();
    }

    public void setId(int id) {
        this.psrRegister.setId(id);
    }

    public PsrProjectStatus getStatus() {
        return this.psrRegister.getStatus();
    }

    public void setStatus(PsrProjectStatus status) {
        this.psrRegister.setStatus(status);
    }

    public GeneralEmployeesEntity getAuthor() {
        return this.psrRegister.getAuthor();
    }

    public void setAuthor(GeneralEmployeesEntity author) {
        this.psrRegister.setAuthor(author);
    }

    public GeneralEmployeesEntity getAdmin() {
        return this.psrRegister.getAdmin();
    }

    public void setAdmin(GeneralEmployeesEntity admin) {
        this.psrRegister.setAdmin(admin);
    }

    public String getName() {
        return this.psrRegister.getName();
    }

    public void setName(String name) {
        this.psrRegister.setName(name);
    }

    public Date getDateOpen() {
        return this.psrRegister.getDateOpen();
    }

    public void setDateOpen(Date dateOpen) {
        this.psrRegister.setDateOpen(dateOpen);
    }

    public Date getDateClose() {
        return this.psrRegister.getDateClose();
    }

    public void setDateClose(Date dateClose) {
        this.psrRegister.setDateClose(dateClose);
    }

    public String getComment() {
        return this.psrRegister.getComment();
    }

    public void setComment(String comment) {
        this.psrRegister.setComment(comment);
    }

    public List<PsrProjectAttachment> getAttachments() {
        return this.psrRegister.getAttachments();
    }

    public void setAttachments(List<PsrProjectAttachment> attachments) {
        this.psrRegister.setAttachments(attachments);
    }

    public List<PsrProjectState> getStateList() {
        return this.psrRegister.getStateList();
    }

    public void setStateList(List<PsrProjectState> stateList) {
        this.psrRegister.setStateList(stateList);
    }
}

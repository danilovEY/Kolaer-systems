package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegister {
     int getId();
     void setId(int id);

     PsrStatus getStatus();
     void setStatus(PsrStatus status);

     GeneralEmployeesEntity getAuthor();
     void setAuthor(GeneralEmployeesEntity author);

     GeneralEmployeesEntity getAdmin();
     void setAdmin(GeneralEmployeesEntity admin);

     String getName();
     void setName(String name);

     Date getDateOpen();

     void setDateOpen(Date dateOpen);
     Date getDateClose();

     void setDateClose(Date dateClose);

     String getComment();
     void setComment(String comment);

     List<PsrAttachment> getAttachments();
     void setAttachments(List<PsrAttachment> attachments);

     List<PsrState> getStateList();
     void setStateList(List<PsrState> stateList);
}

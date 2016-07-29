package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.sql.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegister {
     int getId();
     void setId(int id);

     PsrProjectStatus getStatus();
     void setStatus(PsrProjectStatus status);

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

     List<PsrProjectAttachment> getAttachments();
     void setAttachments(List<PsrProjectAttachment> attachments);

     List<PsrProjectState> getStateList();
     void setStateList(List<PsrProjectState> stateList);
}

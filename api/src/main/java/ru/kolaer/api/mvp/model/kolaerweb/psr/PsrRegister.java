package ru.kolaer.api.mvp.model.kolaerweb.psr;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;

import java.sql.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrRegister {
     int getId();
     void setId(int id);

     PsrProjectStatusBase getStatus();
     void setStatus(PsrProjectStatusBase status);

     GeneralEmployeesEntityBase getAuthor();
     void setAuthor(GeneralEmployeesEntityBase author);

     GeneralEmployeesEntityBase getAdmin();
     void setAdmin(GeneralEmployeesEntityBase admin);

     String getName();
     void setName(String name);

     Date getDateOpen();

     void setDateOpen(Date dateOpen);
     Date getDateClose();

     void setDateClose(Date dateClose);

     String getComment();
     void setComment(String comment);

     List<PsrProjectAttachmentBase> getAttachments();
     void setAttachments(List<PsrProjectAttachmentBase> attachments);

     List<PsrProjectStateBase> getStateList();
     void setStateList(List<PsrProjectStateBase> stateList);
}

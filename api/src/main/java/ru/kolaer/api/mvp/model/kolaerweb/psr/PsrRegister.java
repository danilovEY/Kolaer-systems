package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrRegisterBase.class)
public interface PsrRegister extends Serializable {
     Integer getId();
     void setId(Integer id);

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

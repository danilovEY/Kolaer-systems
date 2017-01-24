package ru.kolaer.api.mvp.model.kolaerweb.webportal.rss;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = WebPortalRssEntityBase.class)
 public interface WebPortalRssEntity extends Serializable {
     int getId();
     void setId(int id);

     EmployeeEntity getUser();
     void setUser(EmployeeEntity user);

     String getTitle();
     void setTitle(String title);

     String getContent();
     void setContent(String content);

     Date getDate();
     void setDate(Date date);

     boolean isHide();
     void setHide(boolean hide);

     int getPriority();
     void setPriority(int priority);
}

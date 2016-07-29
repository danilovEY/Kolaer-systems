package ru.kolaer.api.mvp.model.kolaerweb.webportal.rss;


import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

import java.sql.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
 public interface WebPortalRssEntity {
     int getId();
     void setId(int id);

     GeneralAccountsEntity getUser();
     void setUser(GeneralAccountsEntity user);

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

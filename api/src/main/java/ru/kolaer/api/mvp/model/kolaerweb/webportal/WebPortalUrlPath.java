package ru.kolaer.api.mvp.model.kolaerweb.webportal;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface WebPortalUrlPath {
     int getId();
     void setId(int id);

     String getUrl();
     void setUrl(String url);

     String getDescription();
     void setDescription(String description);

     boolean isAccessAll();
     void setAccessAll(boolean accessAll);

     boolean isAccessSuperAdmin();
     void setAccessSuperAdmin(boolean accessSuperAdmin);

     boolean isAccessAdmin();
     void setAccessAdmin(boolean accessAdmin);

     boolean isAccessUser();
     void setAccessUser(boolean accessUser);

     boolean isAccessAnonymous();
     void setAccessAnonymous(boolean accessAnonymous);
}

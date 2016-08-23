package ru.kolaer.api.mvp.model.kolaerweb.webportal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = WebPortalUrlPathBase.class)
public interface WebPortalUrlPath {
     int getId();
     void setId(int id);

     String getUrl();
     void setUrl(String url);

     String getRequestMethod();
     void setRequestMethod(String method);

     String getDescription();
     void setDescription(String description);

     boolean isAccessAll();
     void setAccessAll(boolean accessAll);

     boolean isAccessSuperAdmin();
     void setAccessSuperAdmin(boolean accessSuperAdmin);

     boolean isAccessPsrAdmin();
     void setAccessPsrAdmin(boolean accessPsrAdmin);

     boolean isAccessUser();
     void setAccessUser(boolean accessUser);

     boolean isAccessAnonymous();
     void setAccessAnonymous(boolean accessAnonymous);
}

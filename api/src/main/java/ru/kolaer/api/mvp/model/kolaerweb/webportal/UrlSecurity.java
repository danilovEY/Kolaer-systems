package ru.kolaer.api.mvp.model.kolaerweb.webportal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = UrlSecurityBase.class)
@ApiModel("Доступ к url")
public interface UrlSecurity extends Serializable {
     int getId();
     void setId(int id);

     String getUrl();
     void setUrl(String url);

     String getRequestMethod();
     void setRequestMethod(String method);

     String getDescription();
     void setDescription(String description);

     List<String> getAccesses();
     void setAccesses(List<String> accesses);
}

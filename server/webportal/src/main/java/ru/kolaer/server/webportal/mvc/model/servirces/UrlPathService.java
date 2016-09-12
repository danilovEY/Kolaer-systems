package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlPathService extends ServiceBase<WebPortalUrlPath> {
    WebPortalUrlPath getPathByUrl(String url);
    List<GeneralRolesEntity> getRoles(WebPortalUrlPath urlPath);

    void createIsNone(WebPortalUrlPath urlPath);

    void clear();
}

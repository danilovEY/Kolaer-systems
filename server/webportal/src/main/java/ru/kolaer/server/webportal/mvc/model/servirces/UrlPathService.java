package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;

import java.util.Collection;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlPathService extends ServiceBase<WebPortalUrlPath> {
    WebPortalUrlPath getPathByUrl(String url);
    List<RoleEntity> getRoles(WebPortalUrlPath urlPath);

    void createOrUpdate(WebPortalUrlPath urlPath);

    void clear();

    void removeAll(Collection<WebPortalUrlPath> values);
}

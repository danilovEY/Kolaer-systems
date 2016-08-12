package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlPathDao extends DaoStandard<WebPortalUrlPath> {
    WebPortalUrlPath getPathByUrl(String url);
    void update(WebPortalUrlPath webPortalUrlPath);

    WebPortalUrlPath getPathByUrlAndMethod(String url, String requestMethod);
}

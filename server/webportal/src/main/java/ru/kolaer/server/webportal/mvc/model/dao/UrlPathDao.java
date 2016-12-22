package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;

import java.util.Collection;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlPathDao extends DefaultDao<WebPortalUrlPath> {
    WebPortalUrlPath getPathByUrl(String url);
    WebPortalUrlPath getPathByUrlAndMethod(String url, String requestMethod);

    void clear();

    void removeAll(Collection<WebPortalUrlPath> values);
}

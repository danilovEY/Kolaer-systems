package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPath;

/**
 * Created by danilovey on 28.07.2016.
 */
public interface UrlPathDao extends DaoStandard<WebPortalUrlPath> {
    WebPortalUrlPath getPathByUrl(String url);
}

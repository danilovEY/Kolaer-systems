package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlPathDao extends DaoStandard<WebPortalUrlPathDecorator> {
    WebPortalUrlPathDecorator getPathByUrl(String url);
}

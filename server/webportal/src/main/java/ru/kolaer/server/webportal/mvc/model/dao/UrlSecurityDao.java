package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlSecurityDao extends DefaultDao<UrlSecurityEntity> {
    UrlSecurityEntity findPathByUrlAndMethod(String url, String method);

    String findAccessByUrlAndMethod(String url, String requestMethod);
}

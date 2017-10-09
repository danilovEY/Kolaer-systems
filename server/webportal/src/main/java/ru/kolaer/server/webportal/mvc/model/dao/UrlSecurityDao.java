package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlSecurityDao extends DefaultDao<UrlSecurityEntity> {
    UrlSecurityEntity getPathByUrl(String url);
    UrlSecurityEntity getPathByUrlAndMethod(String url, String requestMethod);

    void clear();

    List<String> findAllAccessByUrlAndMethod(String url, String requestMethod);

}

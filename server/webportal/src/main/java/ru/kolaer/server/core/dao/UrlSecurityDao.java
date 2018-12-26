package ru.kolaer.server.core.dao;

import ru.kolaer.server.core.model.entity.general.UrlSecurityEntity;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlSecurityDao extends DefaultDao<UrlSecurityEntity> {
    UrlSecurityEntity findPathByUrlAndMethod(String url, String method);

    String findAccessByUrlAndMethod(String url, String requestMethod);

    List<UrlSecurityEntity> findPathByMethod(String method);
}

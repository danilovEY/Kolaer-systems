package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.general.UrlSecurityEntity;

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

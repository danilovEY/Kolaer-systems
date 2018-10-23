package ru.kolaer.server.service.account.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.common.entities.general.UrlSecurityEntity;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlSecurityRepository extends DefaultRepository<UrlSecurityEntity> {
    UrlSecurityEntity findPathByUrlAndMethod(String url, String method);

    String findAccessByUrlAndMethod(String url, String requestMethod);

    List<UrlSecurityEntity> findPathByMethod(String method);
}

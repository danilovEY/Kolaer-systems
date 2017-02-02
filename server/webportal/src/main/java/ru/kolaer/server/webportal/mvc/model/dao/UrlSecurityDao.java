package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;

import java.util.Collection;

/**
 * Created by danilovey on 28.07.2016.
 * Дао для работы с URL.
 */
public interface UrlSecurityDao extends DefaultDao<UrlSecurity> {
    UrlSecurity getPathByUrl(String url);
    UrlSecurity getPathByUrlAndMethod(String url, String requestMethod);

    void clear();

    void removeAll(Collection<UrlSecurity> values);
}

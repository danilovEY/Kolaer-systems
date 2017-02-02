package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;

import java.util.Collection;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlSecurityService extends ServiceBase<UrlSecurity> {
    UrlSecurity getPathByUrl(String url);
    List<RoleEntity> getRoles(UrlSecurity urlPath);

    void createOrUpdate(UrlSecurity urlPath);

    void clear();

    void removeAll(Collection<UrlSecurity> values);
}

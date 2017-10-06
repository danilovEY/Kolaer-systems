package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;

import java.util.Collection;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlSecurityService extends ServiceBase<UrlSecurityDto> {
    UrlSecurityDto getPathByUrl(String url);
    List<UrlSecurityDto> getRoles(UrlSecurityDto urlPath);

    void createOrUpdate(UrlSecurityDto urlPath);

    void clear();

    void removeAll(Collection<UrlSecurityDto> values);
}

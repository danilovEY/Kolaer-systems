package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.UrlSecurityDto;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlSecurityService extends DefaultService<UrlSecurityDto> {
    UrlSecurityDto getPathByUrlAndMethod(String url, String method);
    List<String> getAccesses(UrlSecurityDto urlPath);

    void clear();

    List<UrlSecurityDto> getPathByMethod(String method);
}

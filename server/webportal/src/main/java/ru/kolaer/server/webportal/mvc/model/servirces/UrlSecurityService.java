package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityDto;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface UrlSecurityService extends DefaultService<UrlSecurityDto> {
    UrlSecurityDto getPathByUrl(String url);
    List<String> getAccesses(UrlSecurityDto urlPath);

    void clear();
}

package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.webportal.model.entity.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.model.servirce.BaseConverter;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface UrlSecurityConverter extends BaseConverter<UrlSecurityDto, UrlSecurityEntity> {
    List<String> convertToAccesses(UrlSecurityDto urlPath);
}

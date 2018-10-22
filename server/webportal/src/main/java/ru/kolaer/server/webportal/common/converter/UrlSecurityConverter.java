package ru.kolaer.server.webportal.common.converter;

import ru.kolaer.common.mvp.model.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.webportal.common.entities.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.common.servirces.BaseConverter;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface UrlSecurityConverter extends BaseConverter<UrlSecurityDto, UrlSecurityEntity>{
    List<String> convertToAccesses(UrlSecurityDto urlPath);
}

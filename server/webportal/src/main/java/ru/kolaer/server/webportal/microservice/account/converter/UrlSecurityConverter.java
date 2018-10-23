package ru.kolaer.server.webportal.microservice.account.converter;

import ru.kolaer.common.mvp.model.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.common.entities.general.UrlSecurityEntity;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface UrlSecurityConverter extends BaseConverter<UrlSecurityDto, UrlSecurityEntity> {
    List<String> convertToAccesses(UrlSecurityDto urlPath);
}

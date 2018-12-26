package ru.kolaer.server.core.converter;

import ru.kolaer.common.dto.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.core.model.entity.general.UrlSecurityEntity;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface UrlSecurityConverter extends BaseConverter<UrlSecurityDto, UrlSecurityEntity> {
    List<String> convertToAccesses(UrlSecurityDto urlPath);
}

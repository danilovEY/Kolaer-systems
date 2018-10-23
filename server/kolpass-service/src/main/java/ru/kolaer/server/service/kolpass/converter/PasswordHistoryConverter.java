package ru.kolaer.server.service.kolpass.converter;

import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.service.kolpass.entity.PasswordHistoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface PasswordHistoryConverter extends BaseConverter<PasswordHistoryDto, PasswordHistoryEntity> {
}

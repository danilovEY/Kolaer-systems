package ru.kolaer.server.kolpass.service;

import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.kolpass.model.entity.PasswordHistoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface PasswordHistoryConverter extends BaseConverter<PasswordHistoryDto, PasswordHistoryEntity> {
}

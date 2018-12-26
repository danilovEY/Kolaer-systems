package ru.kolaer.server.kolpass.service;

import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.kolpass.model.entity.PasswordRepositoryEntity;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface PasswordRepositoryConverter extends BaseConverter<PasswordRepositoryDto, PasswordRepositoryEntity> {
}

package ru.kolaer.server.employee.converter;

import ru.kolaer.common.dto.post.PostDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.employee.model.entity.PostEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface PostConverter extends BaseConverter<PostDto, PostEntity> {
}

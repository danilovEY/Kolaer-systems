package ru.kolaer.server.service.post.converter;

import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.service.post.entity.PostEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface PostConverter extends BaseConverter<PostDto, PostEntity> {
}

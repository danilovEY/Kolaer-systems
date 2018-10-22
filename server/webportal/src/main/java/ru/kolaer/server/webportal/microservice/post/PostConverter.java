package ru.kolaer.server.webportal.microservice.post;

import ru.kolaer.common.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface PostConverter extends BaseConverter<PostDto, PostEntity> {
}

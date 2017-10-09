package ru.kolaer.server.webportal.mvc.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface PostConverter extends BaseConverter<PostDto, PostEntity> {
}

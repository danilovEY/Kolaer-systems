package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.converter.PostConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PostDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.PostEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PostService;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PostServiceImpl extends AbstractDefaultService<PostDto, PostEntity, PostDao, PostConverter> implements PostService {

    protected PostServiceImpl(PostDao defaultEntityDao, PostConverter converter) {
        super(defaultEntityDao, converter);
    }

}

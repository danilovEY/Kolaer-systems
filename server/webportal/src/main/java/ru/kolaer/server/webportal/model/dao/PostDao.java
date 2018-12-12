package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.webportal.model.dto.post.FindPostPageRequest;
import ru.kolaer.server.webportal.model.entity.general.PostEntity;

import java.util.List;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostDao extends DefaultDao<PostEntity> {
    List<PostEntity> find(FindPostPageRequest request);

    long findCount(FindPostPageRequest request);
}

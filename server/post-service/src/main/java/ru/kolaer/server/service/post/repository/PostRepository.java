package ru.kolaer.server.service.post.repository;

import ru.kolaer.server.service.post.dto.FindPostPageRequest;
import ru.kolaer.server.service.post.entity.PostEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostRepository extends DefaultRepository<PostEntity> {
    List<PostEntity> find(FindPostPageRequest request);

    long findCount(FindPostPageRequest request);
}

package ru.kolaer.server.webportal.microservice.post.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.post.dto.FindPostPageRequest;
import ru.kolaer.server.webportal.microservice.post.entity.PostEntity;

import java.util.List;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostRepository extends DefaultRepository<PostEntity> {
    List<PostEntity> find(FindPostPageRequest request);

    long findCount(FindPostPageRequest request);
}

package ru.kolaer.server.employee.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.employee.model.entity.PostEntity;
import ru.kolaer.server.employee.model.request.FindPostPageRequest;

import java.util.List;

/**
 * Created by danilovey on 12.10.2017.
 */
public interface PostDao extends DefaultDao<PostEntity> {
    List<PostEntity> find(FindPostPageRequest request);

    long findCount(FindPostPageRequest request);
}

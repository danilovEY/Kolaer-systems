package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordHistoryEntity;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordHistoryDao extends DefaultDao<RepositoryPasswordHistoryEntity> {
    Page<RepositoryPasswordHistoryEntity> findHistoryByIdRepository(Integer id, Integer number, Integer pageSize);

    void deleteByIdRep(Integer id);
}

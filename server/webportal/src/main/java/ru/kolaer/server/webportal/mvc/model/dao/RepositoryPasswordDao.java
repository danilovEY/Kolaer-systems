package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordDao extends DefaultDao<RepositoryPasswordEntity> {
    Page<RepositoryPasswordEntity> findAllByPnumber(Integer pnumber, Integer number, Integer pageSize);

    RepositoryPasswordEntity findByNameAndPnumber(String name, Integer pnumber);

    List<RepositoryPasswordEntity> findAllByPnumbers(List<Integer> idsChief);
}

package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
public interface RepositoryPasswordDao extends DefaultDao<RepositoryPassword> {
    Page<RepositoryPassword> findAllByPnumber(Integer pnumber, Integer number, Integer pageSize);

    RepositoryPassword findByNameAndPnumber(String name, Integer pnumber);

    RepositoryPassword findRepositoryWithJoinById(Integer id);

    List<RepositoryPassword> findAllByPnumbers(List<Integer> idsChief);
}
